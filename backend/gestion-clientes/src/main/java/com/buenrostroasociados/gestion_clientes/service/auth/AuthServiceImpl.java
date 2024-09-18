package com.buenrostroasociados.gestion_clientes.service.auth;

import com.buenrostroasociados.gestion_clientes.dto.auth.SigninRequest;
import com.buenrostroasociados.gestion_clientes.dto.auth.SigninResponse;
import com.buenrostroasociados.gestion_clientes.dto.auth.SignupRequest;
import com.buenrostroasociados.gestion_clientes.entity.*;
import com.buenrostroasociados.gestion_clientes.entity.auth.PasswordResetToken;
import com.buenrostroasociados.gestion_clientes.entity.auth.RefreshToken;
import com.buenrostroasociados.gestion_clientes.events.auth.PasswordConfirmationEvent;
import com.buenrostroasociados.gestion_clientes.events.auth.UserLoginEvent;
import com.buenrostroasociados.gestion_clientes.events.auth.UserRegistrationEvent;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.exception.ResourceNotFoundException;
import com.buenrostroasociados.gestion_clientes.exception.TokenExpiredException;
import com.buenrostroasociados.gestion_clientes.service.email.EmailService;
import com.buenrostroasociados.gestion_clientes.repository.*;
import com.buenrostroasociados.gestion_clientes.repository.auth.PasswordResetTokenRepository;
import com.buenrostroasociados.gestion_clientes.service.jwtBlacklisted.BlacklistedService;
import com.buenrostroasociados.gestion_clientes.service.jwtRefreshToken.RefreshTokenService;
import com.buenrostroasociados.gestion_clientes.service.jwtToken.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService{

    private static final Logger logger= LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AdministradorRepository adminRepo;
    @Autowired
    private ClienteRepository clienteRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private RolRepository rolRepo;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenService jwtService;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BlacklistedService blacklistedService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private final String rolname_ADMIN = "ADMIN", rolnameClient = "CLIENT";

    @Override
    @Transactional
    public void signUp(SignupRequest signupRequest) {
        validateSignupRequest(signupRequest);

        Usuario usuario = new Usuario();
        usuario.setUsername(signupRequest.getUsername());
        //Desactivado por que se ingresa automaticamente el correo asociado dependiendo si es un cliente o un admin
        // usuario.setEmail(signupRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        usuarioRepo.save(usuario);
        assignRoleAndSaveUser(signupRequest, usuario);
        logger.info("User registered successfully: {}", signupRequest.getUsername());
        // Enviar correo de notificación
        eventPublisher.publishEvent(new UserRegistrationEvent(this, usuario.getUsername(), usuario.getEmail()));
    }

    @Override
    public SigninResponse signIn (SigninRequest loginRequest) {
        logger.debug("Attempting to authenticate user: {}", loginRequest.getUsername());

        //autenticacion de usuario
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            logger.info("Obteniendo usuario autenticado...");
            Usuario user = (Usuario) authentication.getPrincipal();
            //Generar el Token de acceso
            logger.info("Generando token para usuario...");
            String token = jwtService.generateToken(user);
            //obtener Rol de usuario
            logger.info("Obteniendo rol de usuario...");
            String role = String.valueOf(user.getRoles().iterator().next().getNombre()); // Obtener el primer rol del usuario

            /// Generar el token de refresco
            logger.info("Generando Token de refresco de usuario...");
            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user);

            // Publicar evento de inicio de sesión exitoso
            logger.info("Publicando Evento ded usuario autenticado...");
            eventPublisher.publishEvent(new UserLoginEvent(this, user.getUsername(), user.getEmail()));

            logger.info("Retornando Respuesta al cleinte...");
            return new SigninResponse(token, refreshToken.getToken(), role);

        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
            throw new BadCredentialsException("Nombre de usuario o contraseña incorrectos");
        } catch (TokenExpiredException ex) {
            logger.error("Token invalido : "+ ex.getMessage());
            throw new TokenExpiredException("Token JWT expirado. Por favor, inicie sesión nuevamente.");
        }
    }

    @Override
    public void sendPasswordResetLink(String email) {

        logger.info("password-reset-request for email: {} ",email);
        logger.info("Generating temporary token for password reset...");

        Usuario user = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Email: "+email));

        String resetToken = generateResetToken(email);
        String resetUrl = "http://localhost:4200/api/v1/auth/public/rescue-account/password-reset/confirm?token="+resetToken;//link del Endpoint para template password-reset-confirm.html
        emailService.sendPasswordResetEmail(email, resetUrl);
        logger.info("Password reset Request email  send successfully to email : {}", email);
    }

    @Override
    public void resetPassword(String token, String newPassword) {

        logger.debug("Resetting password with Token : {} - newPasssword : {} ", token, newPassword);

        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("token no encontrado en el repositorio"));

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token is invalid or expired");
        }

        Usuario usuario = usuarioRepo.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found to reset password confirm..."));

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepo.save(usuario);
        passwordResetTokenRepo.delete(resetToken); // Opcional: elimina el token después de usarlo


        // Eliminar el token de refresco asociado
        RefreshToken refreshTokenOpotional = refreshTokenService.findByToken(token);
        refreshTokenService.deleteByToken(refreshTokenOpotional.getToken());

        eventPublisher.publishEvent(new PasswordConfirmationEvent(this, usuario.getUsername(), usuario.getEmail()));
    }

    @Override
    public void logout (String token){
        Instant expireAt = jwtService.getExpiration(token).toInstant();
        blacklistedService.blacklistToken(token, expireAt);
        logger.info("Token aaded to Blacklist: {}", token);

// Obtener el nombre de usuario del token
        String username = jwtService.getUserName(token);

        // Buscar al usuario y eliminar el token de refresco asociado
        Usuario user = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        RefreshToken refreshTokenOptional = refreshTokenService.findByToken(token);
            refreshTokenService.deleteByToken(refreshTokenOptional.getToken());

    }

    //renovación del access token
    @Override
    public SigninResponse refreshAccessToken(String refreshToken) {
        RefreshToken validToken = refreshTokenService.findByToken(refreshToken);

        // Verificación de la expiración del token de refresco
        if (validToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Refresh token has expired to refresh Token");
        }

        // Generar un nuevo token de acceso usando el usuario asociado
        String newAccessToken = jwtService.generateToken(validToken.getUser());

        return new SigninResponse(newAccessToken, validToken.getToken(), validToken.getUser().getRoles().iterator().next().getNombre());
    }


    /*------------------------- Metods Auxiliares --------------------------*/

    //metodos para SignUp()

    private void validateSignupRequest(SignupRequest signupRequest) {
        logger.debug("Validating signup request for username: {}", signupRequest.getUsername());

        if (usuarioRepo.existsByUsername(signupRequest.getUsername())) {
            logger.error("Username already exists: {}", signupRequest.getUsername());
            throw new IllegalArgumentException("El nombre de usuario ya existe, elige otro");
        }
    }

    private void assignRoleAndSaveUser(SignupRequest signupRequest, Usuario usuario) {
        if (signupRequest.getRfc() != null) {
            /*-- recuperar rol dela base de datos apra asignarlo al user*/
            Rol rol =  rolRepo.findByNombre(rolnameClient).orElseThrow(
                    () -> new EntityNotFoundException("Rol [ "+rolnameClient+" ] no encontrado en el repositorio")
            );

            usuario.getRoles().add(rol);//añadimos rol al contwexto dsspring
            Cliente cliente = clienteRepo.findByRfc(signupRequest.getRfc())
                    .orElseThrow(() -> new EntityNotFoundException("El cliente no se pudo registrar por que el RFC proporcionado no se encuentra en el Repositorio: " + signupRequest.getRfc()));
            cliente.setUsuario(usuario);
            //asopciar usarioa cliente
            clienteRepo.save(cliente);
            //asociar correo cliente a usuario
            usuario.setEmail(cliente.getCorreo());


        } else if (signupRequest.getClaveAdmin() != null) {
            /*-- recuperar rol dela base de datos apra asignarlo al user*/
            Rol rol =  rolRepo.findByNombre(rolname_ADMIN).orElseThrow(
                    () -> new EntityNotFoundException("Rol [ "+rolname_ADMIN+" ] no encontrado en el repositorio")
            );
            usuario.getRoles().add(rol);
            Administrador administrador = adminRepo.findByClave(signupRequest.getClaveAdmin())
                    .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado con ID: " + signupRequest.getClaveAdmin()));

            administrador.setUsuario(usuario);
            //asoicair el usuario al administrador
            adminRepo.save(administrador);
            //asociar el email del admin al usuario
            usuario.setEmail(administrador.getCorreo());
        } else {
            logger.error("Se debe proporcionar al menos un RFC (cliente) or el claveAdmin (administrador)");
            throw new RuntimeException("Debe proporcionar un RFC o una claveAdmin de administrador");
        }



    }

    private String generateResetToken(String email) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Expira en 1 hora
        logger.info("Token generated: {}", resetToken);
        passwordResetTokenRepo.save(resetToken);
        logger.info("Token saved in Repository DB.");
        return token;
    }




}
