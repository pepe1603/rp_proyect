package com.buenrostroasociados.gestion_clientes.utils;

import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserAuthenticated {
    private static final Logger logger = LoggerFactory.getLogger(CurrentUserAuthenticated.class);

    public static Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Usuario no autorizado . no contiene JWT-Token: ");
            throw new UnauthorizedException("Usuario no autenticado con JWT-Token");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Usuario) {
            logger.info("Usuario Authenticated: {}", principal);
            return (Usuario) principal;
        }else {
            logger.warn("Usuario no Autenticado");
            throw new UnauthorizedException("Usuario no autenticado");
        }
    }

    public  static String getEmail(){
        Usuario usuarioAutheticated = CurrentUserAuthenticated.getCurrentUser();
        return usuarioAutheticated.getEmail();

    }

    public static String getEmailUserRolClient(){
        Usuario usuarioAutheticated = CurrentUserAuthenticated.getCurrentUser();

        if (!usuarioAutheticated.getAuthorities().stream().iterator().next().equals("CLIENT")
        ) {
            logger.debug("El ussuairo no cuento con rol cliente: {}", usuarioAutheticated.getAuthorities());

            throw new IllegalArgumentException("El Usuario no es un CLiente");
        } else {
            logger.info("Rol Cliente. se extajo el email...");
            return usuarioAutheticated.getEmail();
        }
    }

}
