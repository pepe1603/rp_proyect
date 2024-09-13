package com.buenrostroasociados.gestion_clientes.service.auth;

import com.buenrostroasociados.gestion_clientes.dto.auth.SigninRequest;
import com.buenrostroasociados.gestion_clientes.dto.auth.SigninResponse;
import com.buenrostroasociados.gestion_clientes.dto.auth.SignupRequest;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {
    @Transactional
    void signUp(SignupRequest signupRequest);

    SigninResponse signIn(SigninRequest loginRequest);

    /*servce  password reset*/
    void sendPasswordResetLink(String email);
    void resetPassword(String token, String newPassword);

    void logout(String token);
}
