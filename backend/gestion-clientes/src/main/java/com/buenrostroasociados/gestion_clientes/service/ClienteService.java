package com.buenrostroasociados.gestion_clientes.service;

import com.buenrostroasociados.gestion_clientes.dto.ClienteDTO;

import java.util.List;
import java.util.Map;

public interface ClienteService {
    ClienteDTO saveCliente(ClienteDTO clienteDTO);
    ClienteDTO getClienteById(Long id);
    ClienteDTO getClienteByRFC(String rfc);
    List<ClienteDTO> getAllClientes();
    ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO);
    ClienteDTO updateCliente(Long id, Map<String, Object> updates);
    void deleteCliente(Long id);

}
