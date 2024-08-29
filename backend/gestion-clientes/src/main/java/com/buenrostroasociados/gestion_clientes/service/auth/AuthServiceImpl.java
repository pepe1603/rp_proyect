package com.buenrostroasociados.gestion_clientes.service.auth;

import com.buenrostroasociados.gestion_clientes.config.security.JwtService;
import com.buenrostroasociados.gestion_clientes.dto.auth.SignupRequest;
import com.buenrostroasociados.gestion_clientes.entity.Administrador;
import com.buenrostroasociados.gestion_clientes.entity.Cliente;
import com.buenrostroasociados.gestion_clientes.entity.Rol;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    AuthenticationManager authManager;

    private final String rolname_ADMIN = "ADMIN", rolnameClient = "CLIENT";

    @Override
    @Transactional
    public void SignUp(SignupRequest signupRequest) {
        validateSignupRequest(signupRequest);

        Usuario usuario = new Usuario();
        usuario.setUsername(signupRequest.getUsername());
        //usuario.setEmail(signupRequest.getEmail());--Deshabilitar en caso de tener un campo emial en usuario
        usuario.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        usuarioRepo.save(usuario);
        assignRoleAndSaveUser(signupRequest, usuario);
        logger.info("User registered successfully: {}", signupRequest.getUsername());
        // Enviar correo de notificación -- Deshabilitar cuandos e implemente EventListenerEmail
        //eventPublisher.publishEvent(new UserRegistrationEvent(this, usuario.getUsername(), usuario.getEmail()));
    }



    /*------------------------- Metods Auxiliares --------------------------*/

    //metodos para SignUp()

    private void validateSignupRequest(SignupRequest signupRequest) {
        logger.debug("Validating signup request for username: {}", signupRequest.getUsername());

        if (usuarioRepo.existsByUsername(signupRequest.getUsername())) {
            logger.error("Username already exists: {}", signupRequest.getUsername());
            throw new IllegalArgumentException("El nombre de usuario ya existe, elige otro");
        }

        /*-leer comentario arriba (deshabilitar en caso de tener campos email en usuario)
        if (signupRequest.getEmail() == null || signupRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El campo email está vacío, se requiere para restablecimiento de contraseña");
        }*/
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
            clienteRepo.save(cliente);
        } else if (signupRequest.getClaveAdmin() != null) {
            /*-- recuperar rol dela base de datos apra asignarlo al user*/
            Rol rol =  rolRepo.findByNombre(rolname_ADMIN).orElseThrow(
                    () -> new EntityNotFoundException("Rol [ "+rolname_ADMIN+" ] no encontrado en el repositorio")
            );
            usuario.getRoles().add(rol);
            Administrador administrador = adminRepo.findByClave(signupRequest.getClaveAdmin())
                    .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado con ID: " + signupRequest.getClaveAdmin()));

            administrador.setUsuario(usuario);
            adminRepo.save(administrador);
        } else {
            logger.error("Se debe proporcionar al menos una matricula or el IDAdmin ");
            throw new RuntimeException("Debe proporcionar una matrícula o un ID de administrador");
        }
    }




}
