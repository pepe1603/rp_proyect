package com.buenrostroasociados.gestion_clientes.service.resource;

import com.buenrostroasociados.gestion_clientes.dto.ActividadContableDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface ActividadContableService {
    ActividadContableDTO saveActividadContable(ActividadContableDTO actividadContableDTO);
    ActividadContableDTO getActividadContableById(Long id);
    List<ActividadContableDTO> getAllActividadesContables();

    ActividadContableDTO updateActividadContable(Long id, ActividadContableDTO actividadContableDTO);

    void deleteActividadContable(Long id);

    Resource exportActividadesToCSV();

    Resource exportActividadesToPDF();
}
