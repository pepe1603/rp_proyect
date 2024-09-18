package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ClienteDTO;
import com.buenrostroasociados.gestion_clientes.dto.UsuarioDTO;
import com.buenrostroasociados.gestion_clientes.dto.auth.SignupRequest;
import com.buenrostroasociados.gestion_clientes.entity.Rol;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.UsuarioMapper;
import com.buenrostroasociados.gestion_clientes.repository.RolRepository;
import com.buenrostroasociados.gestion_clientes.repository.UsuarioRepository;
import com.buenrostroasociados.gestion_clientes.service.ClienteService;
import com.buenrostroasociados.gestion_clientes.service.UsuarioService;
import com.buenrostroasociados.gestion_clientes.service.auth.AuthService;
import com.buenrostroasociados.gestion_clientes.service.export.ExportService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    private final String rolname_ADMIN = "ADMIN", rolnameClient = "CLIENT";
    @Autowired
    private UsuarioRepository usuarioRepo;

    private final UsuarioMapper userMapper;

    private final ExportService exportService;

    private final RolRepository rolRepo;
    private  final PasswordEncoder passwordEncoder;
    private final ClienteService clienteService;
    private final AuthService authService;

    @Override
    public UsuarioDTO saveUsuario(SignupRequest signupRequest) {

//usamos servico authentiocacion apra ello
    authService.signUp(signupRequest);

    Usuario usuarioSaved = usuarioRepo.findByUsername(signupRequest.getUsername())
            .orElseThrow(() -> new EntityNotFoundException("Usuario Recien creado por servicio auth no fue posible recuperarlo"));

        logger.debug("new Uuser saved : {}", signupRequest.getUsername());
       return userMapper.toDTO(usuarioSaved);
    }

    @Override
    public UsuarioDTO getUsuarioById(Long id) {
        Usuario userFounded =  usuarioRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Usuario Not Found with ID: " +id)
        );
        return userMapper.toDTO(userFounded);

    }

    @Override
    public List<UsuarioDTO> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepo.findAll();
        if (usuarios.isEmpty()){
            throw new EntityNotFoundException("No hay usuarios registrados en el sistema.");
        }

        return usuarios
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO) {

        logger.debug("Finding user by Id: {} ", usuarioDTO.getUsername());
        Usuario existingUser = usuarioRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Usuario Not Found with ID: " + id)
        );

        // Verificar si el nuevo username ya está en uso por otro usuario
        if (usuarioDTO.getUsername() != null && !usuarioDTO.getUsername().equals(existingUser.getUsername())) {
            boolean usernameExists = usuarioRepo.existsByUsername(usuarioDTO.getUsername());
            if (usernameExists) {
                throw new IllegalArgumentException("El username ya está en uso: " + usuarioDTO.getUsername());
            }
            existingUser.setUsername(usuarioDTO.getUsername());
        }

        // Encriptar la contraseña antes de actualizarla si es que se ha proporcionado una nueva
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            String encryptedPassword = encryptPassword(usuarioDTO.getPassword());
            existingUser.setPassword(encryptedPassword);
        }
        // Actualizar el email si se ha proporcionado un nuevo valor
        if (usuarioDTO.getEmail() != null) {
            boolean emailExists = usuarioRepo.existsByEmail(usuarioDTO.getEmail());
            if (emailExists && !usuarioDTO.getEmail().equals(existingUser.getEmail())) {
                throw new IllegalArgumentException("El email ya está en uso: " + usuarioDTO.getEmail());
            }

            logger.info("Verificando Cliente Asociado al Correo");
            ClienteDTO clienteDto =  clienteService.getClienteByEmail(usuarioDTO.getEmail());
            if (clienteDto.getCorreo().isEmpty()){
                throw new IllegalArgumentException("Correo del CLiente esta vacio, revisa el cliente Asociado tenga un email valido.");
            }

            existingUser.setEmail(usuarioDTO.getEmail());
            logger.info("User exiting updated fiels Email: "+clienteDto.getCorreo() +" - Automatic until client updates in Email: "+existingUser.getEmail());
        }

        // Actualizar los roles si se proporcionan en el DTO
        if (usuarioDTO.getRoles() != null) {
            Set<Rol> roles = usuarioDTO.getRoles()
                    .stream()
                    .map(rolDTO -> {
                        // Asumimos que tienes un método para buscar roles por nombre o ID
                        return findRoleByName(rolDTO.getNombre());
                    })
                    .collect(Collectors.toSet());
            existingUser.setRoles(roles);
        }

        // Guardar el usuario actualizado en el repositorio
        Usuario updatedUser = usuarioRepo.save(existingUser);
        logger.debug("User updated: {}", updatedUser.getUsername());
        logger.info("Object :"+updatedUser.toString());

        // Convertir la entidad actualizada a DTO y devolverla
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public UsuarioDTO updateUsername(Long userId, String newUsername) {
        logger.debug("Finding user by username: {}", newUsername);
        Usuario usuario = usuarioRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (usuarioRepo.existsByUsername(newUsername)) {
            logger.error("Update failed for user: {}", newUsername);
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        logger.debug("Updating user: {}", usuario.getUsername());

        usuario.setUsername(newUsername);
        usuario = usuarioRepo.save(usuario);
        logger.info("User updated successfully: {}", usuario.getUsername());
        return userMapper.toDTO(usuario);
    }
    @Override
    public UsuarioDTO updatePassword(Long userId, String newPassword) {
        logger.debug("Finding user by ID: {}", userId);
        Usuario usuario = usuarioRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        logger.debug("Updating user: {}", usuario.getUsername());

        usuario.setPassword(encryptPassword(newPassword));
        usuario = usuarioRepo.save(usuario);
        logger.info("Password of User updated successfully: {}", usuario);
        return userMapper.toDTO(usuario);
    }

    @Override
    public void deleteUsuario(Long id) {
        Usuario userToDelete = usuarioRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Usuario Not Found with ID: " + id)
        );

        usuarioRepo.delete(userToDelete);
        logger.debug("User deleted with ID: {}", id);
    }

    @Override
    public List<String> getAllEmails() {
        return usuarioRepo.findAll()
                .stream()
                .map(usuario -> usuario.getEmail())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllAdminEmails() {
        // Obtén la lista de usuarios con el rol ADMIN
        Rol rolAdmin = findRoleByName("ADMIN");

        List<Usuario> admins = usuarioRepo.findByRoles(rolAdmin);
        logger.info("lista de usuarios obetenida para envios de correos electronicos: \n|-=> {}", admins);

        // Extrae y retorna los correos electrónicos de estos usuarios
        return admins.stream()
                .map(Usuario::getEmail)
                .collect(Collectors.toList());
    }
//--------------------- notificar a los admins y al cliente en especifico
    @Override
    public Resource exportUsuariosToCSV() {

        List<Usuario> usuarios = usuarioRepo.findAll();

        if (usuarios.isEmpty()){
            throw new EntityNotFoundException("No hay datos para exportar");
        }


        List<String> headers = List.of("Username", "Password", "Email", "Rol");
        List<List<String>> data = usuarios.stream()
                .map(actividad -> List.of(
                        actividad.getUsername().toString(),
                        actividad.getPassword().toString(),
                        actividad.getEmail().toString(),
                        actividad.getRoles().toString(),
                        actividad.getCliente().toString(),
                        actividad.getAdministrador().toString(),
                        actividad.getAuthorities().toString()
                ))
                .collect(Collectors.toList());
        if (data.isEmpty()) {
            logger.warn("No hay datos para exportar al CSV.");
        }

        return exportService.exportToCSV(headers, data);
    }

    @Override
    public Resource exportUsuariosToPDF() {
        List<Usuario> usuarios = usuarioRepo.findAll();
        if (usuarios.isEmpty()){
            throw new EntityNotFoundException("No hay datos para exportar");
        }


        List<String> headers = List.of("Username", "Password", "Email", "Rol");
        List<List<String>> data = usuarios.stream()
                .map(actividad -> List.of(
                        actividad.getUsername().toString(),
                        actividad.getPassword().toString(),
                        actividad.getEmail().toString(),
                        actividad.getRoles().toString(),
                        actividad.getCliente().toString(),
                        actividad.getAdministrador().toString(),
                        actividad.getAuthorities().toString()
                ))
                .collect(Collectors.toList());
        if (data.isEmpty()) {
            logger.warn("No hay datos para exportar al PDF.");
        }

        String title = "Reporte de Usuarios";
        return exportService.exportToPDF(title, headers, data);
    }



    //---------- Metohods Aux
    // Método de encriptación de contraseñas (debes tener una implementación adecuada)
    private String encryptPassword(String password) {
        // Ejemplo usando BCrypt
        return passwordEncoder.encode(password);
    }

    // Método para encontrar un rol por nombre
    private Rol findRoleByName(String roleName) {
        return rolRepo.findByNombre(roleName).orElseThrow(
                () -> new EntityNotFoundException("Rol no encontrado con nombre: "+roleName)
        );

    }


}
