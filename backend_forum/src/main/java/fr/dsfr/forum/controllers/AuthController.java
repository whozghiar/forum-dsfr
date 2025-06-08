package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.dto.AuthDTO.RegisterUserDTO;
import fr.dsfr.forum.beans.dto.AuthDTO.LoginRequestDTO;
import fr.dsfr.forum.beans.dto.AuthDTO.LoginResponseDTO;
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

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserDTO dto) {
        keycloakAdminService.createUser(dto.getUsername(), dto.getEmail(), dto.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        var token = keycloakAdminService.login(dto.getUsername(), dto.getPassword());
        LoginResponseDTO response = new LoginResponseDTO(token.getToken(), token.getRefreshToken(), token.getIdToken());
        return ResponseEntity.ok(response);
    }
}
