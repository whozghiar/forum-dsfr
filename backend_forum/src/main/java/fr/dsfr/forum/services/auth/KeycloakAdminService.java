package fr.dsfr.forum.services.auth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    @Value("${keycloak.admin.url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String username;

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
                .clientId("admin-cli")
                .username(username)
                .password(password)
                .build();
    }

    public void createUser(String login, String email, String rawPassword) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(rawPassword);
        credentials.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(login);
        user.setEmail(email);
        user.setEnabled(true);
        user.setCredentials(Collections.singletonList(credentials));

        usersResource.create(user);
    }

    public AccessTokenResponse login(String user, String pass) {
        Keycloak kc = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .username(user)
                .password(pass)
                .build();
        return kc.tokenManager().getAccessToken();
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
     * Vérifie si l'utilisateur actuellement connecté possède le rôle d'administrateur.
     * Cette méthode utilise le contexte de sécurité pour obtenir l'authentification
     * et parcourt les autorités accordées à l'utilisateur afin de déterminer
     * si le rôle `ROLE_ADMIN` est présent.
     *
     * @return `true` si l'utilisateur possède le rôle d'administrateur, sinon `false`.
     */
    public static boolean isAdministrateur() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}
