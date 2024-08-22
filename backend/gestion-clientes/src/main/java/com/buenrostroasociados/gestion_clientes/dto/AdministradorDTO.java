package com.buenrostroasociados.gestion_clientes.dto;

import com.buenrostroasociados.gestion_clientes.utils.PhoneNumberValidatorUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdministradorDTO {
    private Long id;
    @NotBlank(message = "La clave no puede estar en blanco")
    private String clave;
    @NotBlank(message = "Elñ nombre no deebe de esatr en blanco")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombreFull;
    @NotBlank(message = "El correo no puede estar en blanco")
    @Email(message = "El correo electrónico no es válido")
    private String correo;
    @NotBlank(message = "El número de teléfono no puede estar en blanco")
    private String telefono;
    private Long usuarioId; //referenciamos a Usuaio

    /**
     * Verifica si el número de teléfono es válido.
     * @return true si el número es válido, false en caso contrario.
     */
    public boolean isPhoneNumberValid(String regionCode) {
        return PhoneNumberValidatorUtil.isValidPhoneNumber(this.telefono, regionCode);
    }

}
