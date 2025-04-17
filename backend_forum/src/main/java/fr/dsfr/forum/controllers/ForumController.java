package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Forum;
import fr.dsfr.forum.beans.dto.ForumDTO.CreerForumDTO;
import fr.dsfr.forum.beans.dto.ForumDTO.ForumReponseDTO;
import fr.dsfr.forum.beans.dto.ForumDTO.ModifierForumDTO;
import fr.dsfr.forum.services.EntityValidatorService;
import fr.dsfr.forum.services.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forums")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;
    private final EntityValidatorService validator;

    @GetMapping
    public ResponseEntity<List<ForumReponseDTO>> getAllForums() {
        List<ForumReponseDTO> dtos = forumService.getAllForums()
                .stream()
                .map(ForumReponseDTO::convertir)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{forumId}")
    public ResponseEntity<ForumReponseDTO> getForumById(@PathVariable Long forumId) {
        Forum forum = validator.getForumOrThrow(forumId);
        return ResponseEntity.ok(ForumReponseDTO.convertir(forum));
    }

    @PostMapping("/create")
    public ResponseEntity<ForumReponseDTO> createForum(@RequestBody CreerForumDTO dto) {
        Forum forum = new Forum();
        forum.setTitre(dto.getTitre());
        forum.setDescription(dto.getDescription());

        Forum created = forumService.createForum(forum);
        return ResponseEntity.status(HttpStatus.CREATED).body(ForumReponseDTO.convertir(created));
    }

    @PutMapping("/{forumId}/update")
    public ResponseEntity<ForumReponseDTO> updateForum(
            @PathVariable Long forumId,
            @RequestBody CreerForumDTO dto) {

        validator.getForumOrThrow(forumId);

        Forum forum = new Forum();
        forum.setTitre(dto.getTitre());
        forum.setDescription(dto.getDescription());

        Forum updated = forumService.updateForumById(forumId, forum);
        return ResponseEntity.ok(ForumReponseDTO.convertir(updated));
    }

    @PatchMapping("/{forumId}/patch")
    public ResponseEntity<ForumReponseDTO> patchForum(
            @PathVariable Long forumId,
            @RequestBody ModifierForumDTO dto) {

        Forum forum = validator.getForumOrThrow(forumId);

        if (dto.getTitre() != null) {
            forum.setTitre(dto.getTitre());
        }

        if (dto.getDescription() != null) {
            forum.setDescription(dto.getDescription());
        }

        Forum updated = forumService.updateForumById(forumId, forum);

        // Convertit le forum mis à jour en DTO
        ForumReponseDTO reponse = ForumReponseDTO.convertir(updated);

        return ResponseEntity.ok(reponse);
    }

    @DeleteMapping("/{forumId}/delete")
    public ResponseEntity<Void> deleteForum(@PathVariable Long forumId) {
        validator.getForumOrThrow(forumId);
        forumService.deleteForum(forumId);
        return ResponseEntity.noContent().build();
    }





}
