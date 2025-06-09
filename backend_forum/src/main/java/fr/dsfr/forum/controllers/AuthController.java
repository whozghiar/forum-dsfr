package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.dto.AuthDTO.EnregistrementUtilisateurDTO;
import fr.dsfr.forum.beans.dto.AuthDTO.RequeteConnexionDTO;
import fr.dsfr.forum.beans.dto.AuthDTO.ReponseLoginDTO;
import fr.dsfr.forum.services.auth.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakAdminService keycloakAdminService;

    @PostMapping("/inscription")
    public ResponseEntity<Void> register(@RequestBody EnregistrementUtilisateurDTO dto) {
        keycloakAdminService.createUser(dto.getPseudonyme(), dto.getEmail(), dto.getMotDePasse());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/connexion")
    public ResponseEntity<ReponseLoginDTO> login(@RequestBody RequeteConnexionDTO dto) {
        var token = keycloakAdminService.login(dto.getPseudonyme(), dto.getMotDePasse());
        ReponseLoginDTO response = new ReponseLoginDTO(token.getToken(), token.getRefreshToken(), token.getIdToken());
        return ResponseEntity.ok(response);
    }
}
