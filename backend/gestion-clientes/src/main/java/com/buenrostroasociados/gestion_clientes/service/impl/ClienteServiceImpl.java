package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ClienteDTO;
import com.buenrostroasociados.gestion_clientes.entity.Cliente;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.ClienteMapper;
import com.buenrostroasociados.gestion_clientes.repository.ClienteRepository;
import com.buenrostroasociados.gestion_clientes.repository.UsuarioRepository;
import com.buenrostroasociados.gestion_clientes.service.ClienteService;
import com.buenrostroasociados.gestion_clientes.service.export.ExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);

    @Autowired
    private ClienteRepository clienteRepo;
    @Autowired
    private ClienteMapper clienteMapper;
    @Autowired
    private ExportService exportService;
    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    public ClienteDTO saveCliente(ClienteDTO clienteDTO) {
        Cliente newCliente = clienteMapper.toEntity(clienteDTO);
        newCliente.setUsuario(null);
        Cliente savedCliente = clienteRepo.save(newCliente);

        return clienteMapper.toDTO(savedCliente);
    }

    @Override
    public ClienteDTO getClienteById(Long id) {
        Cliente clienteFounded = clienteRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente no encontrado con ID : "+id)
                );
        return clienteMapper.toDTO(clienteFounded);
    }

    @Override
    public ClienteDTO getClienteByRFC(String rfc) {
        if ( rfc == null || rfc.isEmpty() ){
            throw new IllegalArgumentException("El RFC no puede estar en blanco");
        }
        Cliente clienteFounded = clienteRepo.findByRfc(rfc)
                .orElseThrow(
                       () -> new EntityNotFoundException("Cliente no encontrado con RFC: "+rfc)
                );
        return clienteMapper.toDTO(clienteFounded);
    }

    @Override
    public List<ClienteDTO> getAllClientes() {
        List<Cliente> clientes = clienteRepo.findAll();
        if (clientes.isEmpty()){
            throw new EntityNotFoundException("No se Encontraron Clientes en el Repositorio");
        }
        logger.debug("Clientes recuperados: {}", clientes);
        return clientes.stream()
                .map(clienteMapper::toDTO)
                .collect( Collectors.toList() );
    }

    @Override
    public ClienteDTO getClienteByEmail(String email){
        Cliente clienteFounded = clienteRepo.findByCorreo(email).orElseThrow(() -> new EntityNotFoundException("Cliente Not Found with Email: "+ email));
        return  clienteMapper.toDTO(clienteFounded);
    }

    @Override
    public ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO) {
        Cliente clienteFounded = clienteRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente no encontrado con ID: "+id)
                );

        if (!(clienteDTO.getTelefono().isEmpty())){
            clienteFounded.setTelefono(clienteDTO.getTelefono());
        }
        clienteFounded.setNombreFull(clienteDTO.getNombreFull());
        if (!clienteDTO.getCorreo().isEmpty()){
            clienteFounded.setCorreo(clienteDTO.getCorreo());
        }


        clienteFounded.setRfc(clienteDTO.getRfc());

        Cliente updatedCliente = clienteRepo.save(clienteFounded);
        //una vez que el cleinte ya ha sido actualizadoc on exito actaulizamos el email del usuario
        logger.warn("Verificando actualizacion del usuario si esta disponible");
        if (!(clienteDTO.getUsuarioId() == null)){
            Usuario usuarioFounded = usuarioRepo.findById(clienteDTO.getUsuarioId()).orElseThrow(
                    () -> new EntityNotFoundException("Usuario no encontrado Para Cliente : "+clienteDTO.getUsuarioId())
            ) ;

            usuarioFounded.setEmail(updatedCliente.getCorreo());
            logger.warn("Correo del usuario actualizado automaticamnte");
        }

        return  clienteMapper.toDTO(updatedCliente);

    }

    @Override
    public void deleteCliente(Long id) {
        Cliente clienteFounded = clienteRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("El cliente no se pudo eliminar por que no fue encontrado con ID: "+id)
                );

        clienteRepo.deleteById(id);
    }

    @Override
    public Resource exportActividadesToCSV() {
        List<ClienteDTO> clientes = getAllClientes();
        List<String> headers = List.of("ID", "RFC", "NombreCompleto", "Correo", "Telefono");
        List<List<String>> data = clientes.stream()
                .map(cliente -> List.of(
                        cliente.getId().toString(),
                        cliente.getRfc().toString(),
                        cliente.getNombreFull().toString(),
                        cliente.getCorreo().toString(),
                        cliente.getTelefono().toString()
                ))
                .collect(Collectors.toList());

        if (data.isEmpty()) {
            logger.warn("No hay datos para exportar al CSV.");
        }

        return exportService.exportToCSV(headers, data);
    }

    @Override
    public Resource exportActividadesToPDF() {
        List<ClienteDTO> clientes = getAllClientes();
        List<String> headers = List.of("ID", "RFC", "NombreCompleto", "Correo", "Telefono");
        List<List<String>> data = clientes.stream()
                .map(cliente -> List.of(
                        cliente.getId().toString(),
                        cliente.getRfc().toString(),
                        cliente.getNombreFull().toString(),
                        cliente.getCorreo().toString(),
                        cliente.getTelefono().toString()
                ))
               .collect(Collectors.toList());

        String title = "Reporte de Clientes";
        return exportService.exportToPDF(title, headers, data);
    }



}
