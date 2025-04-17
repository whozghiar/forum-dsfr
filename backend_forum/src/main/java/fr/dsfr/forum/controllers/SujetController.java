package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Forum;
import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.beans.dto.CreerSujetDTO;
import fr.dsfr.forum.beans.dto.ModifierSujetDTO;
import fr.dsfr.forum.beans.dto.SujetReponseDTO;
import fr.dsfr.forum.services.EntityValidatorService;
import fr.dsfr.forum.services.SujetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forums/{forumId}/sujets")
@RequiredArgsConstructor
public class SujetController {

    private final SujetService sujetService;
    private final EntityValidatorService validator;

    @GetMapping
    public ResponseEntity<List<SujetReponseDTO>> getSujetsParForum(@PathVariable Long forumId) {
        validator.getForumOrThrow(forumId);

        List<SujetReponseDTO> dtos = sujetService.getSujetByForumId(forumId)
                .stream()
                .map(SujetReponseDTO::convertir)
                .toList();

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{sujetId}")
    public ResponseEntity<SujetReponseDTO> getSujet(@PathVariable Long forumId, @PathVariable Long sujetId) {
        Sujet sujet = validator.getSujetInForumOrThrow(sujetId, forumId);
        return ResponseEntity.ok(SujetReponseDTO.convertir(sujet));
    }

    @PostMapping("/create")
    public ResponseEntity<SujetReponseDTO> createSujet(
            @PathVariable Long forumId,
            @RequestBody CreerSujetDTO dto) {

        Forum forum = validator.getForumOrThrow(forumId);

        Sujet sujet = new Sujet();
        sujet.setTitre(dto.getTitre());
        sujet.setForum(forum);

        Sujet created = sujetService.createSujet(sujet);

        // On convertit le sujet créé en DTO
        SujetReponseDTO reponse = SujetReponseDTO.convertir(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

    @PutMapping("/{sujetId}/update")
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
