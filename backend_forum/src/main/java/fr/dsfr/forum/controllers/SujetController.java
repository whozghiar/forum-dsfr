package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Forum;
import fr.dsfr.forum.beans.Message;
import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.beans.dto.SujetDTO.CreerSujetDTO;
import fr.dsfr.forum.beans.dto.SujetDTO.ModifierSujetDTO;
import fr.dsfr.forum.beans.dto.SujetDTO.SujetReponseDTO;
import fr.dsfr.forum.services.EntityValidatorService;
import fr.dsfr.forum.services.SujetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/forums/{forumId}/sujets")
@RequiredArgsConstructor
@Slf4j
public class SujetController {

    private final SujetService sujetService;
    private final EntityValidatorService validator;


    @GetMapping("/{sujetId}")
    public ResponseEntity<SujetReponseDTO> getSujet(@PathVariable Long forumId, @PathVariable Long sujetId) {
        Sujet sujet = validator.getSujetInForumOrThrow(sujetId, forumId);
        return ResponseEntity.ok(SujetReponseDTO.convertir(sujet));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_UTILISATEUR')")
    public ResponseEntity<SujetReponseDTO> createSujet(
            @PathVariable Long forumId,
            @RequestBody CreerSujetDTO dto) {
        Forum forum = validator.getForumOrThrow(forumId);

        // Création du sujet
        Sujet sujet = new Sujet();
        sujet.setTitre(dto.getTitre());
        sujet.setForum(forum);

        // Création du message associé
        Message message = new Message();
        message.setContenu(dto.getMessage());
        message.setDateCreation(LocalDateTime.now());
        message.setAuteur(validator.getAuteurOrThrow(dto.getAuteurId()));
        message.setSujet(sujet);

        sujet.getMessages().add(message);

        Sujet created = sujetService.createSujet(forumId, sujet);

        // On convertit le sujet créé en DTO
        SujetReponseDTO reponse = SujetReponseDTO.convertir(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

    @PutMapping("/{sujetId}/update")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<SujetReponseDTO> updateSujet(
            @PathVariable Long forumId,
            @PathVariable Long sujetId,
            @RequestBody CreerSujetDTO dto) {

        validator.getSujetInForumOrThrow(sujetId, forumId);

        Sujet sujet = new Sujet();
        sujet.setTitre(dto.getTitre());
        sujet.setForum(validator.getForumOrThrow(forumId));

        Sujet updated = sujetService.updateSujetById(sujetId, sujet);
        return ResponseEntity.ok(SujetReponseDTO.convertir(updated));
    }

    @PatchMapping("/{sujetId}/patch")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<SujetReponseDTO> patchSujet(
            @PathVariable Long forumId,
            @PathVariable Long sujetId,
            @RequestBody ModifierSujetDTO dto) {

        // Valide que le sujet existe et qu'il appartient bien au forum
        Sujet sujet = validator.getSujetInForumOrThrow(sujetId, forumId);

        if(dto.getTitre() != null) {
            sujet.setTitre(dto.getTitre());
        }

        Sujet updated = sujetService.updateSujetById(sujetId, sujet);

        // Convertit le sujet mis à jour en DTO
        SujetReponseDTO reponse = SujetReponseDTO.convertir(updated);

        return ResponseEntity.ok(reponse);
    }

    @DeleteMapping("/{sujetId}/delete")
    public ResponseEntity<Void> deleteSujet(@PathVariable Long forumId, @PathVariable Long sujetId) {
        validator.getSujetInForumOrThrow(sujetId, forumId);
        sujetService.deleteSujet(sujetId);
        return ResponseEntity.noContent().build();
    }


}
