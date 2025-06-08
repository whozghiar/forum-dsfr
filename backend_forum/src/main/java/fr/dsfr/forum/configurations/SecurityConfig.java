package fr.dsfr.forum.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure la chaîne de filtres de sécurité pour l'application.
     * Cette méthode définit les règles de sécurité, notamment :
     * - Autorisation de toutes les requêtes.
     * - Configuration des règles CORS via une source de configuration.
     * - Désactivation de la protection CSRF.
     *
     * @param http L'objet HttpSecurity utilisé pour configurer la sécurité.
     * @return Une instance de `SecurityFilterChain` contenant la configuration de sécurité.
     * @throws Exception Si une erreur survient lors de la configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Autorise toutes les requêtes sans restriction
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                // Configure les règles CORS en utilisant une source de configuration
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                // Désactive la protection CSRF
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

/**
         * Définit une source de configuration CORS pour l'application.
         * Cette méthode configure les règles CORS, notamment les origines autorisées,
         * les méthodes HTTP autorisées, les en-têtes autorisés, et la durée de mise en cache
         * des réponses préflight.
         *
         * @return Une instance de `CorsConfigurationSource` contenant les règles CORS configurées.
         */
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            // Création d'une nouvelle configuration CORS
            CorsConfiguration configuration = new CorsConfiguration();

            // Définition des origines autorisées
            configuration.setAllowedOrigins(List.of("http://localhost:5174"));

            // Définition des méthodes HTTP autorisées
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

            // Définition des en-têtes autorisés
            configuration.setAllowedHeaders(List.of("Content-Type", "Authorization"));

            // Autorisation des cookies et des informations d'identification
            configuration.setAllowCredentials(true);

            // Durée de mise en cache des réponses préflight (en secondes)
            configuration.setMaxAge(3600L);

            // Association de la configuration CORS à un chemin spécifique
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/api/**", configuration);

            // Retourne la source de configuration CORS
            return source;
        }

    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || !realmAccess.containsKey("roles")) {
                return Collections.emptyList();
            }
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet());
        });
        return converter;
    }
}