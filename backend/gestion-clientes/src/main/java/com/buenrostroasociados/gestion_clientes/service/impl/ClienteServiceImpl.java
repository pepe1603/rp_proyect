package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ClienteDTO;
import com.buenrostroasociados.gestion_clientes.entity.Cliente;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.ClienteMapper;
import com.buenrostroasociados.gestion_clientes.repository.ClienteRepository;
import com.buenrostroasociados.gestion_clientes.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepo;
    @Autowired
    private ClienteMapper clienteMapper;

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
        return clientes.stream()
                .map(clienteMapper::toDTO)
                .collect( Collectors.toList() );
    }

    @Override
    public ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO) {
        Cliente clienteFounded = clienteRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente no encontrado con ID: "+id)
                );

        clienteFounded.setNombreFull(clienteDTO.getNombreFull());
        clienteFounded.setCorreo(clienteDTO.getCorreo());
        clienteFounded.setRfc(clienteDTO.getRfc());

        Cliente updatedCliente = clienteRepo.save(clienteFounded);
        return  clienteMapper.toDTO(updatedCliente);

    }

    @Override
    public ClienteDTO updateCliente(Long id, Map<String, Object> updates) {
        Cliente clienteFounded = clienteRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente no encontrado  con ID: "+id)
                );

        //aplñicamos actualizaciones
        if (updates.containsKey("nombre")) {
            clienteFounded.setNombreFull((String) updates.get("nombre"));
        }else if (updates.containsKey("correo")){
            clienteFounded.setCorreo((String) updates.get("correo"));
        }else{
            throw new IllegalArgumentException("Campo no valido para la actualizacion.");
        }

        Cliente updatedCliente = clienteRepo.save(clienteFounded);

return clienteMapper.toDTO(updatedCliente);
    }

    @Override
    public void deleteCliente(Long id) {
        Cliente clienteFounded = clienteRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("El cliente no se pudo eliminar por que no fue encontrado con ID: "+id)
                );

        clienteRepo.deleteById(id);
    }

}
