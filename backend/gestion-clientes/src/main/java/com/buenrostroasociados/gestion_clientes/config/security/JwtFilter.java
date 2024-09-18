package com.buenrostroasociados.gestion_clientes.config.security;

import com.buenrostroasociados.gestion_clientes.exception.JwtTokenBlacklistedException;
import com.buenrostroasociados.gestion_clientes.service.jwtBlacklisted.BlacklistedService;
import com.buenrostroasociados.gestion_clientes.service.jwtToken.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtService;
    private final BlacklistedService blacklistService;

    @Autowired
    public JwtFilter(UserDetailsService userDetailsService, JwtTokenService jwtService, BlacklistedService blacklistService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.blacklistService = blacklistService;
        logger.info("JwtFilter initialized");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        logger.debug("Processing request : " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader != null) {
            logger.info("Authorization header found: {}", authHeader);
        } else {
            logger.warn("Authorization header missing");
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No JWT token found, passing request through.");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        // Verificamos que el token no está en la lista negra
        if (blacklistService.isTokenBlacklisted(jwt)) {
            throw new JwtTokenBlacklistedException("Token has been blacklisted");
        }

        username = jwtService.getUserName(jwt);

        logger.debug("JWT token found, username: {}", username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                logger.debug("User authenticated successfully: {}", username);
            } else {
                logger.warn("Invalid JWT token for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
        // Logging after processing
        logger.info("Request processing completed: {} {}", request.getMethod(), request.getRequestURI());
    }
}
