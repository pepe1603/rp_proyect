package com.buenrostroasociados.gestion_clientes.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;


public class PhoneNumberValidatorUtil {
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    /**
     * Valida si el número de teléfono proporcionado es válido.
     * @param phoneNumber Número de teléfono a validar.
     * @param regionCode Código de región (por ejemplo, "US" para Estados Unidos, "MX" para México).
     * @return true si el número es válido, false en caso contrario.
     */

    public static boolean isValidPhoneNumber(String phoneNumber, String regionCode) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, regionCode);
            return phoneNumberUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
    }
}
