package com.buenrostroasociados.gestion_clientes.exception;

import com.buenrostroasociados.gestion_clientes.dto.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /*- ecxeptiones para clases no encontradas ene l reposotory*/

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleEntityNotFound(EntityNotFoundException ex){
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found Entity", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgumentsException(IllegalArgumentException ex){
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CustomErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex){
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE.value(), "Payload Too Large","Verifica el Tamaño de los archivos."+ ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGeneralException(Exception ex){
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Ocurrio un error inesperado."+ ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**- manejo de archivos*/
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<CustomErrorResponse> handleFileStorageException(FileStorageException ex){
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Ocurrio un error inesperado de tipo Archivo."+ ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found Resource",  ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**- unthorized */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<CustomErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized request", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.FORBIDDEN.value(), "Forbidden", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Not Acceptable", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    /*
    * Exceptions Jwt
    * */
    @ExceptionHandler(JwtTokenBlacklistedException.class)
    public ResponseEntity<CustomErrorResponse> handleJwtTokenBlacklisted(JwtTokenBlacklistedException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


}
