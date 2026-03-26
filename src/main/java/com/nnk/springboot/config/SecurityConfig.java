package com.nnk.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     *
     * <p>Authentification session-based : à la connexion, Spring Security crée une session
     * HTTP côté serveur et retourne un cookie {@code JSESSIONID} au navigateur.
     * Chaque requête suivante est authentifiée via ce cookie sans re-saisie des identifiants.
     * La session est invalidée à la déconnexion.</p>
     *
     * <p>Règles d'accès :</p>
     * <ul>
     *   <li>{@code /login}, ressources statiques : accessibles sans authentification</li>
     *   <li>{@code /admin/**} : réservé au rôle {@code ADMIN}</li>
     *   <li>Toutes les autres routes : tout utilisateur authentifié ({@code USER} ou {@code ADMIN})</li>
     * </ul>
     *
     * <p>En cas d'accès refusé (403), Spring Boot résout automatiquement la vue
     * {@code error/403.html} via son mécanisme d'erreur par défaut.</p>
     *
     * @param http l'objet HttpSecurity à configurer
     * @return la SecurityFilterChain configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                )
                .logout(logout -> logout
                        .logoutUrl("/app-logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
