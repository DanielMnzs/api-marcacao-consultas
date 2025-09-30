// Caminho: src/main/java/com/fiap/eca/api_marcacao_consultas/security/JwtAuthenticationFilter.java
package com.fiap.eca.api_marcacao_consultas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // A LÓGICA DE LIBERAR ROTAS FOI REMOVIDA DAQUI.
        // Agora, o filtro SEMPRE tenta validar o token.
        // O SecurityConfig que vai decidir se a rota é pública ou não.

        String token = extractToken(request);

        if (token != null && jwtTokenProvider.validarToken(token)) {
            String email = jwtTokenProvider.obterEmailDoToken(token);
            // Autentica o usuário no contexto de segurança do Spring para esta requisição
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // Continua a cadeia de filtros, independentemente de ter autenticado ou não.
        // O Spring Security vai usar o contexto que populamos (ou não) para tomar a decisão final.
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}