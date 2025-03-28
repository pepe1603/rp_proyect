package com.buenrostroasociados.gestion_clientes.service.resource;

import com.buenrostroasociados.gestion_clientes.dto.AdministradorDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface AdministradorService {
    AdministradorDTO savedAdministrador(AdministradorDTO administradorDTO);
    AdministradorDTO getAdministradorById(Long id);
    List<AdministradorDTO> getAllAdministradores();
    AdministradorDTO getAdministradorByEmail(String email);
    AdministradorDTO updateClaveAdministrador(Long id, String clave);
    AdministradorDTO updateAdministrador(Long id, AdministradorDTO administradorDTO);
    void deleteAdministrador(Long id);

    Resource exportActividadesToCSV();

    Resource exportActividadesToPDF();
}
