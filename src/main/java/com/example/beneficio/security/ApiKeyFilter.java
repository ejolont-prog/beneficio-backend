package com.example.beneficio.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${api.agricultor.key:CLAVE_POR_DEFECTO_SEGURA}")
    private String apiKeyValida;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Filtramos solo para el endpoint específico de recepción
        if (request.getRequestURI().contains("/api/recepcion-pesaje")) {
            String requestKey = request.getHeader("X-API-KEY");

            if (apiKeyValida.equals(requestKey)) {
                // Si la clave es correcta, dejamos pasar
                filterChain.doFilter(request, response);
            } else {
                // Si no, bloqueamos con 401
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("API Key invalida para comunicacion externa");
            }
        } else {
            // Si es cualquier otro endpoint, que siga el flujo normal (donde actuará el JwtFilter)
            filterChain.doFilter(request, response);
        }
    }
}