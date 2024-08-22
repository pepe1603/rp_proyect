package com.buenrostroasociados.gestion_clientes.service;

import com.buenrostroasociados.gestion_clientes.dto.AdministradorDTO;

import java.util.List;
import java.util.Map;

public interface AdministradorService {
    AdministradorDTO savedAdministrador(AdministradorDTO administradorDTO);
    AdministradorDTO getAdministradorById(Long id);
    List<AdministradorDTO> gatAllAdministradores();
    AdministradorDTO getAdministradorByEmail(String email);
    AdministradorDTO updateClaveAdministrador(Long id, String clave);
    AdministradorDTO updateAdministrador(Long id, AdministradorDTO administradorDTO);
    void deleteAdministrador(Long id);

}
