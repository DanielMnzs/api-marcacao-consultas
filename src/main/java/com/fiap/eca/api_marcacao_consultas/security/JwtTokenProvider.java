// Conteúdo de "Plano B" para JwtTokenProvider.java
package com.fiap.eca.api_marcacao_consultas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String gerarToken(String email) {
        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email) // <- Note que aqui é setSubject
                .setIssuedAt(agora)
                .setExpiration(dataExpiracao)
                .signWith(getSigningKey())
                .compact();
    }

    public String obterEmailDoToken(String token) {
        Claims claims = Jwts.parser()  // <- Usando o método antigo .parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()  // <- Usando o método antigo .parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}