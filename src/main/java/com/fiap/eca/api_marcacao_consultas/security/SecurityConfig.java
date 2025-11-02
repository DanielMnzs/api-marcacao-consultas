// Caminho: src/main/java/com/fiap/eca/api_marcacao_consultas/security/SecurityConfig.java
package com.fiap.eca.api_marcacao_consultas.security;

// 🔥 MUDANÇA: Importamos o UsuarioService
import com.fiap.eca.api_marcacao_consultas.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Imports que a gente adicionou para o CORS funcionar
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

// Esse import estava duplicado, deixei só o List
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioService usuarioService; // 🔥 MUDANÇA: Adicionamos o service

    // 🔥 MUDANÇA: Injetamos o UsuarioService no construtor
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UsuarioService usuarioService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.usuarioService = usuarioService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 🔓 Endpoints públicos (não precisam de token)
                .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll() // Cadastro de novos usuários
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/readings/**").permitAll() // Requisito da Sprint

                // 🔐 Qualquer outra requisição PRECISA de um token válido
                .anyRequest().authenticated()
            )
            // 🔥 CORREÇÃO (PARTE 1): Deixamos SÓ ESSE .cors()
            // Esse é o que chama o @Bean que a gente criou
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // necessário p/ H2 console

            // 🔥 MUDANÇA: Passamos o usuarioService para o construtor do filtro
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, usuarioService), UsernamePasswordAuthenticationFilter.class);
            
            // 🔥 CORREÇÃO (PARTE 2): O BLOCO DUPLICADO .cors() FOI REMOVIDO DAQUI.
            // Aquele bloco que estava aqui antes estava sobrescrevendo
            // a configuração certa e causando o erro.

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Em SecurityConfig.java
    // Esse é o @Bean que o .cors() ali de cima usa.
    // Ele está correto.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 👇 AQUI ESTÁ O PULO DO GATO
        // Você libera a origem do seu frontend (vi na imagem que é localhost:8081)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081")); 
        
        // Libera os métodos que seu frontend vai usar
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        
        // Libera os cabeçalhos que seu frontend pode mandar
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        
        // Permite que o frontend envie credenciais (como cookies ou tokens)
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // Aplica essa configuração para TODAS as rotas ("/**")
        source.registerCorsConfiguration("/**", configuration); 
        
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}