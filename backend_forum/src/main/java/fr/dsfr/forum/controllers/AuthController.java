package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.dto.AuthDTO.EnregistrementUtilisateurDTO;
import fr.dsfr.forum.beans.dto.AuthDTO.RequeteConnexionDTO;
import fr.dsfr.forum.beans.dto.AuthDTO.ReponseLoginDTO;
import fr.dsfr.forum.services.auth.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final KeycloakAdminService keycloakAdminService;

    @PostMapping("/inscription")
    public ResponseEntity<Void> register(@RequestBody EnregistrementUtilisateurDTO dto) {
        log.info("Tentative d'enregistrement d'un nouvel utilisateur : {}", dto.getPseudonyme());
        keycloakAdminService.createUser(dto.getPseudonyme(), dto.getEmail(), dto.getMotDePasse());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/connexion")
    public ResponseEntity<ReponseLoginDTO> login(@RequestBody RequeteConnexionDTO dto) {
        log.info("Tentative de connexion de l'utilisateur : {}", dto.getPseudonyme());
        var token = keycloakAdminService.login(dto.getPseudonyme(), dto.getMotDePasse());
        ReponseLoginDTO response = new ReponseLoginDTO(token.getToken(), token.getRefreshToken(), token.getIdToken());
        return ResponseEntity.ok(response);
    }
}
