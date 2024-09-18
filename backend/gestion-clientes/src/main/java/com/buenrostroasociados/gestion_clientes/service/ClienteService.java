package com.buenrostroasociados.gestion_clientes.service;

import com.buenrostroasociados.gestion_clientes.dto.ClienteDTO;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

public interface ClienteService {
    ClienteDTO saveCliente(ClienteDTO clienteDTO);
    ClienteDTO getClienteById(Long id);
    ClienteDTO getClienteByRFC(String rfc);
    List<ClienteDTO> getAllClientes();

    ClienteDTO getClienteByEmail(String email);

    ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO);

    void deleteCliente(Long id);

    Resource exportActividadesToCSV();

    Resource exportActividadesToPDF();
}
