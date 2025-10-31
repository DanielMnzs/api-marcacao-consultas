// Caminho: src/main/java/com/fiap/eca/api_marcacao_consultas/security/JwtAuthenticationFilter.java
package com.fiap.eca.api_marcacao_consultas.security;

// 🔥 MUDANÇA: Imports necessários
import com.fiap.eca.api_marcacao_consultas.service.UsuarioService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

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
    private final UsuarioService usuarioService; // 🔥 MUDANÇA: Adicionamos o service

    // 🔥 MUDANÇA: Recebemos o UsuarioService no construtor
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UsuarioService usuarioService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && jwtTokenProvider.validarToken(token)) {
            String email = jwtTokenProvider.obterEmailDoToken(token);

            // 🔥 MUDANÇA: Buscamos o usuário no banco pelo email
            com.fiap.eca.api_marcacao_consultas.model.Usuario usuario = usuarioService.buscarPorEmail(email);

            // 🔥 MUDANÇA: Criamos a lista de permissões com o "tipo" (Role) do usuário
            // A Role precisa ser prefixada com "ROLE_" se você usar @PreAuthorize("hasRole('ADMIN')")
            // Mas para autenticação simples baseada no tipo, só o tipo já basta (ex: "ADMIN")
            // Vou usar o tipo direto, mas se der pau, a gente bota "ROLE_" + usuario.getTipo()
            List<SimpleGrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority(usuario.getTipo()));

            // 🔥 MUDANÇA: Autenticamos o usuário no contexto com as permissões corretas
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
            
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

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