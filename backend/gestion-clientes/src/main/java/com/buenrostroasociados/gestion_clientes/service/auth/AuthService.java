package com.buenrostroasociados.gestion_clientes.service.auth;

import com.buenrostroasociados.gestion_clientes.dto.auth.SignupRequest;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {
    @Transactional
    void SignUp(SignupRequest signupRequest);
}
