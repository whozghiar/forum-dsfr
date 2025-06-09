package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Auteur;
import fr.dsfr.forum.beans.Message;
import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.beans.dto.MessageDTO.CreerMessageDTO;
import fr.dsfr.forum.beans.dto.MessageDTO.MessageReponseDTO;
import fr.dsfr.forum.beans.dto.MessageDTO.ModifierMessageDTO;
import fr.dsfr.forum.services.EntityValidatorService;
import fr.dsfr.forum.services.MessageService;
import fr.dsfr.forum.services.auth.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/forums/{forumId}/sujets/{sujetId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final EntityValidatorService validator;



    @GetMapping()
    public ResponseEntity<List<MessageReponseDTO>> getMessagesBySujetOfForum(
            @PathVariable Long forumId,
            @PathVariable Long sujetId) {

        // Valide que le sujet existe et qu'il appartient bien au forum
        validator.getSujetInForumOrThrow(sujetId, forumId);

        // Récupère les messages liés à ce sujet
        List<MessageReponseDTO> dtos = messageService.getMessageBySujetId(sujetId)
                .stream()
                .map(MessageReponseDTO::convertir)
                .sorted((m1, m2) -> m1.getDateCreation().compareTo(m2.getDateCreation()))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageReponseDTO> getMessageById(@PathVariable Long messageId) {
        Message message = validator.getMessageOrThrow(messageId);
        return ResponseEntity.ok(MessageReponseDTO.convertir(message));
    }


    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_UTILISATEUR')")
    public ResponseEntity<MessageReponseDTO> createMessageInSujetOfForum(
            @PathVariable Long forumId,
            @PathVariable Long sujetId,
            @RequestBody CreerMessageDTO dto) {

        // Valide que le sujet existe et qu'il appartient bien au forum
        Sujet sujet = validator.getSujetInForumOrThrow(sujetId, forumId);

        // Valide que l'auteur existe
        Auteur auteur = validator.getAuteurOrThrow(dto.getAuteurId());

        // Vérifie que l'auteur du message est bien l'utilisateur connecté
        if(!KeycloakAdminService.isAuteur(auteur.getPseudo())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Crée le message à partir du DTO
        Message message = new Message();
        message.setContenu(dto.getContenu());
        message.setAuteur(auteur);
        message.setSujet(sujet);
        message.setDateCreation(LocalDateTime.now());

        Message created = messageService.createMessage(message);

        // Convertit le message créé en DTO
        MessageReponseDTO reponse = MessageReponseDTO.convertir(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

    @PutMapping("/{messageId}/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_UTILISATEUR')")
    public ResponseEntity<MessageReponseDTO> updateMessageInSujetOfForum(
            @PathVariable Long forumId,
            @PathVariable Long sujetId,
            @PathVariable Long messageId,
            @RequestBody CreerMessageDTO dto) {

        // Valide que le sujet existe et qu'il appartient bien au forum
        Sujet sujet = validator.getSujetInForumOrThrow(sujetId, forumId);

        // Valide que l'auteur existe
        Auteur auteur = validator.getAuteurOrThrow(dto.getAuteurId());

        // Vérifie que l'auteur du message est bien l'utilisateur connecté
        if(!KeycloakAdminService.isAuteur(auteur.getPseudo())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Valide que le message existe et qu'il appartient bien au sujet
        Message message = validator.getMessageOrThrow(messageId);

        // Met à jour le message à partir du DTO
        message.setContenu(dto.getContenu());
        message.setAuteur(auteur);
        message.setSujet(sujet);
        message.setDateCreation(LocalDateTime.now());

        Message updated = messageService.updateMessageById(messageId, message);

        // Convertit le message mis à jour en DTO
        MessageReponseDTO reponse = MessageReponseDTO.convertir(updated);

        return ResponseEntity.ok(reponse);
    }

    @PatchMapping("/{messageId}/patch")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_UTILISATEUR')")
    public ResponseEntity<MessageReponseDTO> patchMessageInSujetOfForum(
            @PathVariable Long forumId,
            @PathVariable Long sujetId,
            @PathVariable Long messageId,
            @RequestBody ModifierMessageDTO dto) {

        // Valide que le sujet existe et qu'il appartient bien au forum
        Sujet sujet = validator.getSujetInForumOrThrow(sujetId, forumId);

        // Valide que le message existe et qu'il appartient bien au sujet
        Message message = validator.getMessageOrThrow(messageId);

        // Vérifie que l'auteur du message est bien l'utilisateur connecté
        if(!KeycloakAdminService.isAuteur(message.getAuteur().getPseudo())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(dto.getContenu() != null) {
            message.setContenu(dto.getContenu());
        }

        if(dto.getAuteurId() != null) {
            // Valide que l'auteur existe
            Auteur auteur = validator.getAuteurOrThrow(dto.getAuteurId());
            message.setAuteur(auteur);
        }

        if(dto.getSujetId() != null) {
            // Valide que le sujet existe
            message.setSujet(sujet);
        }

        // Mise à jour (date de création non modifiée)
        Message updated = messageService.updateMessageById(messageId, message);

        // Convertit le message mis à jour en DTO
        MessageReponseDTO reponse = MessageReponseDTO.convertir(updated);

        return ResponseEntity.ok(reponse);
    }

    @DeleteMapping("/{messageId}/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_UTILISATEUR')")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        validator.getMessageOrThrow(messageId);
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

}
