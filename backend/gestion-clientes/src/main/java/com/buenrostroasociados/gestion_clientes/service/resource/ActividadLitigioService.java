package com.buenrostroasociados.gestion_clientes.service.resource;

import com.buenrostroasociados.gestion_clientes.dto.ActividadLitigioDTO;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActividadLitigioService {

    ActividadLitigioDTO saveActividadLitigio(ActividadLitigioDTO actividadLitigioDTO);
    ActividadLitigioDTO getActividadLitigioById(Long id);
    List<ActividadLitigioDTO> getAllActividadesLitigio();
    ActividadLitigioDTO updateActividadLitigio(Long id, ActividadLitigioDTO actividadLitigioDTO);
    @Transactional
    void updateEstadoActividadLitigio(Long id, String estadoCaso);

    void deleteActividadLitigio(Long id);

    Resource exportActividadesToCSV();

    Resource exportActividadesToPDF();
}
