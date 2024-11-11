package com.buenrostroasociados.gestion_clientes.service.awss3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.buenrostroasociados.gestion_clientes.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3ServiceImpl implements S3Service{

    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucket.name}")
    private String s3BucketName;

    @Override
    public void uploadFile(String fileName, Resource resource) {
        try {
            // Crear un PutObjectRequest con el archivo y sus metadatos
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(resource.contentLength());  // Establece el tamaño del archivo

            // Subir el archivo a S3 usando InputStream directamente
            s3Client.putObject(new PutObjectRequest(s3BucketName, fileName, resource.getInputStream(), metadata));
            logger.info("Archivo subido exitosamente: {}", fileName);
        } catch (IOException e) {
            // Manejo de errores de IO
            logger.error("Error al leer el recurso del archivo: {}", fileName, e);
            throw new RuntimeException("Error al leer el recurso del archivo", e);
        } catch (Exception e) {
            // Manejo de errores generales
            logger.error("Error al subir el archivo a S3: {}", fileName, e);
            throw e;
        }
    }


    @Override
    public Resource downloadFile(String fileName) {
        try {
            if (!fileExists(fileName)) {
                throw new ResourceNotFoundException("El archivo no existe en el bucket: " + fileName);
            }
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(s3BucketName, fileName));
            InputStream inputStream = s3Object.getObjectContent();
            return new InputStreamResource(inputStream);
        } catch (AmazonS3Exception e) {
            logger.error("Error al descargar el archivo de S3: {}", fileName, e);
            throw new RuntimeException("Error al descargar el archivo de S3", e);
        }
    }




    @Override
    public void deleteFile(String fileName) {
        try {
            // Verifica si el archivo existe antes de eliminarlo
            if (!fileExists(fileName)) {
                logger.warn("El archivo no existe en S3, no se puede eliminar: {}", fileName);
                return;
            }
            s3Client.deleteObject(new DeleteObjectRequest(s3BucketName, fileName));
            logger.info("Archivo eliminado exitosamente de S3: {}", fileName);
        } catch (AmazonS3Exception e) {
            logger.error("Error al intentar eliminar el archivo de S3: {}", fileName, e);
            throw new RuntimeException("Error al eliminar el archivo de S3", e);
        } catch (Exception e) {
            logger.error("Error inesperado al intentar eliminar el archivo de S3: {}", fileName, e);
            throw e;
        }
    }


    @Override
    public List<String> listFiles() {
        try {
            // Crear la solicitud para listar objetos en el bucket
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(s3BucketName);

            // Obtener la lista de objetos
            ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);

            // Obtener los nombres de los archivos (keys)
            return objectListing.getObjectSummaries().stream()
                    .map(S3ObjectSummary::getKey)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al listar archivos en S3", e);
            throw new RuntimeException("Error al listar archivos en S3", e);
        }
    }

    @Override
    public boolean fileExists(String fileName) {
        return s3Client.doesObjectExist(s3BucketName, fileName);
    }

}
