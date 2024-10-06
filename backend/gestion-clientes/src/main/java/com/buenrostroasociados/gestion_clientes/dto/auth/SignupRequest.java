package com.buenrostroasociados.gestion_clientes.dto.auth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank(message = "el username no debe estar vacio o null")
    @Size(min = 3, max = 40, message = "El password requiere un size entre 6 y 40 caracteres")
    private String username;

    @NotBlank(message="El password no debe estar vacio o null")
    @Size(min = 6, max = 40, message = "El password requiere un size entre 6 y 40 caracteres")
    private String password;

    @NotBlank(message = "El email no debe estar vacio o null")
    @Email(message = "El valor ingresado no es un Email")
    private String email;

    //validar si es un administrador o un alumno registrado en la base dee datos
    private String rfc; //en caso de ser alumno
    private String claveAdmin; //en caso de ser administrador
}
