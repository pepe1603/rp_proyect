package com.buenrostroasociados.gestion_clientes.dto.auth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Email
    private String email;

    //validar si es un administrador o un alumno registrado en la base dee datos
    private String rfc; //en caso de ser alumno
    private String claveAdmin; //en caso de ser administrador
}
