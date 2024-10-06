package com.buenrostroasociados.gestion_clientes.service.auth;

import com.buenrostroasociados.gestion_clientes.dto.RolDTO;
import com.buenrostroasociados.gestion_clientes.dto.UsuarioDTO;
import com.buenrostroasociados.gestion_clientes.dto.auth.RefreshTokenDTO;
import com.buenrostroasociados.gestion_clientes.dto.auth.SigninRequest;
import com.buenrostroasociados.gestion_clientes.dto.auth.SigninResponse;
import com.buenrostroasociados.gestion_clientes.dto.auth.SignupRequest;
import com.buenrostroasociados.gestion_clientes.entity.*;
import com.buenrostroasociados.gestion_clientes.entity.auth.PasswordResetToken;
import com.buenrostroasociados.gestion_clientes.entity.auth.RefreshToken;
import com.buenrostroasociados.gestion_clientes.events.auth.PasswordConfirmationEvent;
import com.buenrostroasociados.gestion_clientes.events.auth.UserLoginEvent;
import com.buenrostroasociados.gestion_clientes.events.auth.UserLogoutEvent;
import com.buenrostroasociados.gestion_clientes.events.auth.UserRegistrationEvent;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.exception.ResourceNotFoundException;
import com.buenrostroasociados.gestion_clientes.exception.TokenExpiredException;
import com.buenrostroasociados.gestion_clientes.mapper.RefreshTokenMapper;
import com.buenrostroasociados.gestion_clientes.service.email.EmailService;
import com.buenrostroasociados.gestion_clientes.repository.*;
import com.buenrostroasociados.gestion_clientes.repository.auth.PasswordResetTokenRepository;
import com.buenrostroasociados.gestion_clientes.service.jwtBlacklisted.BlacklistedService;
import com.buenrostroasociados.gestion_clientes.service.jwtRefreshToken.RefreshTokenService;
import com.buenrostroasociados.gestion_clientes.service.jwtToken.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService{

    private static final Logger logger= LoggerFactory.getLogger(AuthServiceImpl.class);

    //url decliente vuejs /auth/ForgotPassword
    @Value("${app-client.reset-password-url}")
    private String resetPasswordUrl;
    private final String rolname_ADMIN = "ADMIN", rolnameClient = "CLIENT";
    @Value("${password.reset.token.expiration.hours}")
    private long resetTokenExpirationHours;


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
    @Autowired
    private RefreshTokenMapper refreshTokenMapper;


    @Override
    @Transactional
    public void signUp(SignupRequest signupRequest) {
        validateSignupRequest(signupRequest);

        Usuario usuario = new Usuario();
        usuario.setUsername(signupRequest.getUsername());
        //Desactivado por que se ingresa automaticamente el correo asociado dependiendo si es un cliente o un admin
        usuario.setEmail(signupRequest.getEmail());
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
            logger.info("Generando token de Acceso para usuario...");
            String token = jwtService.generateToken(user);
            //obtener Rol de usuario
            logger.info("Obteniendo rol de usuario...");

            /// Generar el token de refresco
            logger.info("Generando Token de refresco de usuario...");
            RefreshTokenDTO refreshTokenDTO = refreshTokenService.generateRefreshToken(user);

            //creamos Usuario DTo para devolver en la respuesta

            UsuarioDTO userData = new UsuarioDTO();
            userData.setId(user.getId());
            userData.setUsername(user.getUsername());
            userData.setPassword("Password Encryted");
            userData.setEmail(user.getEmail());
            userData.setRoles(user.getRoles().stream()
                    .map(rol -> new RolDTO(rol.getId(), rol.getNombre())) // Esto debe retorna un Set<RolDTO>
                    .collect(Collectors.toSet()));

            // Publicar evento de inicio de sesión exitoso
            logger.info("Publicando Evento de usuario autenticado...");
            eventPublisher.publishEvent(new UserLoginEvent(this, user.getUsername(), user.getEmail()));

            logger.info("Retornando Respuesta Sign_in al cliente ...");
            return new SigninResponse(token, refreshTokenDTO.getToken(), userData);

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
        //URL Vuejs -> auth/forgot-poasword para restablecer la contraseña
        String resetUrl =  resetPasswordUrl + "?tokenReset=" + resetToken;

        //enviar link al email
        emailService.sendPasswordResetEmail(user, resetUrl);
        logger.info("Reset URL send to:  {} \n User: {}", resetUrl, user.getUsername());
        logger.info("Password reset Request email  send successfully to email : {}", email);
    }

    @Transactional
    @Override
    public void resetPassword(String tokenReset, String newPassword) {

        logger.debug("Resetting password with Token-reset: {} - newPasssword to user: {} ", tokenReset, newPassword);
        //validar Nueva password
        validateNewPassword(newPassword);

        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(tokenReset)
                .orElseThrow(() -> new ResourceNotFoundException("token-reset no encontrado en el repositorio"));

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token-reset is invalid or expired");
        }

        Usuario usuario = usuarioRepo.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found to reset password confirm..."));

        logger.info("Realizando cambios en Usuario.. {}", usuario.getUsername());
        usuario.setPassword(passwordEncoder.encode(newPassword));
        logger.warn("Password encrypted and changed...");
        usuarioRepo.save(usuario);
        logger.warn("Preparin Deleting Token Refresh...");

        //Eliminamos todos los token ded refresco del usuario
        refreshTokenService.deleteAllByUser(usuario);
        passwordResetTokenRepo.delete(resetToken); // Opcional: elimina el token de restablecimiento después de usarlo
        logger.info("The refresh Deleted succesfully..");

       eventPublisher.publishEvent(new PasswordConfirmationEvent(this, usuario.getUsername(), usuario.getEmail()));


    }

    @Override
    public void logout (String token){
        // extraer el nombre de usuario del token
        String username = jwtService.getUserName(token);

        // Buscar al usuario y eliminar el token de refresco asociado
        Usuario user = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found to logout "));

        ///Invalidar el token de acceso (agregamos a BlackList)
        Instant expireAt = jwtService.getExpiration(token).toInstant();
        blacklistedService.blacklistToken(token, expireAt);
        logger.info("Token added to Blacklist: {}", token);

        //Buscar y ELiminar el token de refresco asociado
        RefreshTokenDTO refreshTokenOptional = refreshTokenService.findByUser(user);
            refreshTokenService.deleteByToken(refreshTokenOptional.getToken());
        // Publicar evento de inicio de sesión exitoso
        logger.info("Publicando Evento de cierre de sesión de usuario...");
        eventPublisher.publishEvent(new UserLogoutEvent(this, user.getUsername(), user.getEmail()));

    }

    //renovación del access token
    @Override
    public SigninResponse refreshAccessToken(String refreshToken) {
        RefreshToken validToken = refreshTokenMapper.toEntity(refreshTokenService.findByToken(refreshToken));

        // Verificación de la expiración del token de refresco
        if (validToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.error("Refresh Token has expired to refresh Toekn: {}", validToken.getToken());
            throw new TokenExpiredException("Refresh token has expired to refresh Token");
        }

        // Generar un nuevo token de acceso usando el usuario asociado
        String newAccessToken = jwtService.generateToken(validToken.getUser());

        // Crear UsuarioDTO
        UsuarioDTO userData = new UsuarioDTO();
        userData.setId(validToken.getUser().getId());
        userData.setUsername(validToken.getUser().getUsername());
        userData.setPassword("Password Encrypted");
        userData.setEmail(validToken.getUser().getEmail());
        userData.setRoles(validToken.getUser().getRoles().stream()
                .map(rol -> new RolDTO(rol.getId(), rol.getNombre()))
                .collect(Collectors.toSet()));

        return new SigninResponse(newAccessToken, validToken.getToken(), userData);
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
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(resetTokenExpirationHours)); // Expira en 1 hora

        logger.info("Reset Token generated: {}", resetToken);
        passwordResetTokenRepo.save(resetToken);
        logger.info("Reset Token saved in Repository DB.");
        return token;
    }

    private void validateNewPassword(@org.jetbrains.annotations.NotNull String newPassword) {
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres");
        }
        // Agregar más reglas según sea necesario
    }



}
