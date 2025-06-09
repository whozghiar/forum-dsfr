package fr.dsfr.forum.services.auth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminService {

    @Value("${keycloak.admin.url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String username;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${keycloak.admin.password}")
    private String password;

    @Value("${keycloak.client-id}")
    private String clientId;

    private Keycloak keycloak;

    @PostConstruct
    public void init() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret("MWfWcioBClLfMhCAp4TsfUX7rzPBrHbh")
                .username(username)
                .password(password)
                .build();
    }

    /**
     * Crée un nouvel utilisateur dans le Keycloak.
     *
     * Cette méthode utilise l'API Admin de Keycloak pour créer un utilisateur avec les informations fournies.
     * Elle configure les informations d'identification, active l'utilisateur et gère la réponse HTTP.
     *
     * @param login Le nom d'utilisateur du nouvel utilisateur.
     * @param email L'adresse e-mail du nouvel utilisateur.
     * @param rawPassword Le mot de passe brut du nouvel utilisateur.
     * @throws RuntimeException Si la création de l'utilisateur échoue ou si le statut HTTP n'est pas 201.
     */
    public void createUser(String login, String email, String rawPassword) {
        // Récupération des ressources du realm et des utilisateurs via l'API Keycloak
        log.debug("Récupération des ressources du realm et des utilisateurs pour le realm : {}", realm);
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Configuration des informations d'identification de l'utilisateur
        log.debug("Configuration des informations d'identification pour l'utilisateur : {}", login);
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(rawPassword);
        credentials.setTemporary(false);

        // Création de la représentation de l'utilisateur
        log.debug("Création de la représentation de l'utilisateur avec login : {}, email : {}", login, email);
        UserRepresentation user = new UserRepresentation();
        user.setUsername(login);
        user.setEmail(email);
        user.setEnabled(true);
        user.setCredentials(Collections.singletonList(credentials));

        // Envoi de la requête de création de l'utilisateur et gestion de la réponse
        log.debug("Envoi de la requête de création de l'utilisateur : {}", user);
        try (var response = usersResource.create(user)) {
            if (response.getStatus() != 201) {
                throw new RuntimeException("Erreur lors de la création de l'utilisateur : " + response.getStatusInfo());
            }
        }
    }

    /**
     * Authentifie un utilisateur auprès de Keycloak et récupère un jeton d'accès.
     *
     * Cette méthode utilise les informations d'identification de l'utilisateur pour
     * se connecter à Keycloak et obtenir un jeton d'accès. Elle valide les entrées,
     * gère les exceptions et garantit la fermeture du client Keycloak.
     *
     * @param user Le nom d'utilisateur pour l'authentification.
     * @param pass Le mot de passe de l'utilisateur.
     * @return Un objet AccessTokenResponse contenant le jeton d'accès et les informations associées.
     * @throws IllegalArgumentException Si le nom d'utilisateur ou le mot de passe est invalide.
     * @throws RuntimeException Si une erreur survient lors de l'authentification.
     */
    public AccessTokenResponse login(String user, String pass) {
        // Validation des entrées utilisateur
        log.debug("Tentative de connexion avec l'utilisateur : {}", user);

        if (user == null || user.isBlank() || pass == null || pass.isBlank()) {
            log.warn("Nom d'utilisateur ou mot de passe invalide");
            throw new IllegalArgumentException("Utilisateur ou mot de passe invalide.");
        }
        // Création et fermeture automatique du client Keycloak
        log.debug("Création du client Keycloak pour l'authentification");
        try (Keycloak kc = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret("MWfWcioBClLfMhCAp4TsfUX7rzPBrHbh")
                .username(user)
                .password(pass)
                .build()) {
            // Récupération du jeton d'accès via le gestionnaire de jetons
            log.debug("Récupération du jeton d'accès pour l'utilisateur : {}", user);
            return kc.tokenManager().getAccessToken();
        } catch (Exception e) {
            log.error("Erreur lors de l'authentification de l'utilisateur {} : {}", user, e.getMessage());
            // Gestion des erreurs d'authentification
            throw new RuntimeException("Erreur lors de l'authentification : " + e.getMessage(), e);
        }
    }

    /**
     * Récupère le nom d'utilisateur de l'utilisateur actuellement connecté.
     * Cette méthode utilise le contexte de sécurité pour obtenir l'authentification
     * et extrait la revendication `preferred_username` du jeton JWT si disponible.
     *
     * @return Le nom d'utilisateur de l'utilisateur connecté, ou `null` si aucune
     *         authentification n'est disponible ou si le jeton ne contient pas la revendication.
     */
    public static String getUtilisateurConnecte(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth instanceof JwtAuthenticationToken token){
            return token.getToken().getClaimAsString("preferred_username");
        }
        return null;
    }

    /**
     * Vérifie si l'utilisateur courant dispose du rôle ADMINISTRATOR en se basant
     * sur le jeton JWT fourni.
     *
     * @param token le jeton JWT de l'utilisateur
     * @return true si le rôle ADMINISTRATOR est présent, false sinon
     */
    public boolean isAdmin(String token) {
        if (token == null || token.isBlank()) {
            log.warn("Le token fourni est vide ou nul");
            return false;
        }
        log.debug("Vérification du rôle ADMINISTRATOR pour le token : {}", token);

        JwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        Jwt jwt = decoder.decode(token.replace("Bearer ", ""));
        var realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            log.warn("Accès aux rôles du realm non trouvé dans le jeton JWT");
            return false;
        }
        var roles = (Iterable<String>) realmAccess.get("roles");
        for (String r : roles) {
            if ("ADMINISTRATOR".equals(r) || "ADMIN".equals(r)) {
                log.info("L'utilisateur a le rôle ADMINISTRATOR");
                return true;
            }
        }
        return false;
    }

    /**
     * Indique si le pseudonyme fourni correspond à l'utilisateur authentifié.
     *
     * @param pseudo le pseudonyme à comparer
     * @return true si le pseudonyme correspond à l'utilisateur connecté
     */
    public static boolean isAuteur(String pseudo) {
        String current = getUtilisateurConnecte();
        log.info("Vérification si l'utilisateur '{}' est l'auteur '{}'", current, pseudo);
        return current != null && current.equals(pseudo);
    }
}
