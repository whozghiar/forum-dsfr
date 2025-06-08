package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.dto.AuthDTO.RegisterUserDTO;
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
}
