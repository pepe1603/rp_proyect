package com.buenrostroasociados.gestion_clientes.service;

import com.buenrostroasociados.gestion_clientes.dto.ArchivoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArchivoService {

    ArchivoDTO saveArchivo(ArchivoDTO archivoDTO, MultipartFile file, boolean replaceExisting);

    ArchivoDTO getArchivo(Long id);
    List<ArchivoDTO> getAllArchivos();

    List<ArchivoDTO> getArchivosByActividadContableId(Long actividadContableId);

    List<ArchivoDTO> getArchivosByActividadLitigioId(Long actividadLitigioId);

    ArchivoDTO updateArchivo(Long id, ArchivoDTO archivoDTO, MultipartFile file, boolean replaceExisting);

    ArchivoDTO updateArchivoMetadata(Long id, ArchivoDTO archivoDTO);

    void deleteArchivo(Long id);
}

/*
*3. Actualización del Frontend

Para que el frontend pueda manejar esta funcionalidad:

    Verificación de Existencia: Implementa una llamada a la API para verificar si el archivo ya existe en el servidor antes de la carga.
    Interfaz de Usuario: Muestra un mensaje de confirmación al usuario si el archivo ya existe y pregúntale si desea reemplazarlo o no. Dependiendo de la respuesta, realiza la llamada a la API con el parámetro replaceExisting adecuado.
    Carga de Nuevas Versiones: Permite al usuario cargar nuevas versiones y gestionar el historial de versiones si es necesario.

Aquí tienes un ejemplo básico en el frontend utilizando JavaScript y una librería como Axios para manejar la API:

javascript

async function uploadFile(file) {
    const response = await axios.get(`/api/archivos/check?filename=${file.name}`);

    if (response.data.exists) {
        const userConfirmed = confirm('El archivo ya existe. ¿Deseas reemplazarlo?');
        if (!userConfirmed) {
            return;
        }
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('replaceExisting', userConfirmed || false);  // Enviar el estado de reemplazo

    await axios.post('/api/archivos', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

En este ejemplo, se hace una solicitud para verificar si el archivo ya existe antes de realizar la carga y, si es necesario, se muestra una confirmación al usuario.
Conclusión

Implementar la verificación de existencia y la opción de reemplazo de archivos permite una mayor flexibilidad y control sobre los archivos en tu aplicación. Asegúrate de probar cuidadosamente estas funcionalidades para garantizar una experiencia de usuario fluida y sin errores.
* async function uploadFile(file) {
    const response = await axios.get(`/api/archivos/check?filename=${file.name}`);

    if (response.data.exists) {
        const userConfirmed = confirm('El archivo ya existe. ¿Deseas reemplazarlo?');
        if (!userConfirmed) {
            return;
        }
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('replaceExisting', userConfirmed || false);  // Enviar el estado de reemplazo

    await axios.post('/api/archivos', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

* */