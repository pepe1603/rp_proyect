package com.buenrostroasociados.gestion_clientes.service.awss3;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface S3Service {
    // Subir archivo
    void uploadFile(String fileName, Resource resource);

    // Descargar archivo
    Resource downloadFile(String fileName);

    // Eliminar archivo
    void deleteFile(String fileName);

    // Listar archivos
    List<String> listFiles();

    boolean fileExists(String fileName);
}
