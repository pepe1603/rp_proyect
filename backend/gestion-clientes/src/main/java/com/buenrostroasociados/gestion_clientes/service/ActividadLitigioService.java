package com.buenrostroasociados.gestion_clientes.service;

import com.buenrostroasociados.gestion_clientes.dto.ActividadLitigioDTO;

import java.util.List;
import java.util.Map;

public interface ActividadLitigioService {

    ActividadLitigioDTO saveActividadLitigio(ActividadLitigioDTO actividadLitigioDTO);
    ActividadLitigioDTO getActividadLitigioById(Long id);
    List<ActividadLitigioDTO> getAllActividadesLitigio();
    ActividadLitigioDTO updateActividadLitigio(Long id, ActividadLitigioDTO actividadLitigioDTO);
    ActividadLitigioDTO updateActividadLitigio(Long id, Map<String , Object> updates);
    void deleteActividadLitigio(Long id);
}
