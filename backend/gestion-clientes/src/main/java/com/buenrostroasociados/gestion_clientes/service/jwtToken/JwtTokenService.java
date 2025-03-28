package com.buenrostroasociados.gestion_clientes.service.jwtToken;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface JwtTokenService {
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extractClaims, UserDetails userDetails);
    String getUserName(String token);
    boolean validateToken(String token, UserDetails userDetails);
    Date getExpiration(String token);
}
