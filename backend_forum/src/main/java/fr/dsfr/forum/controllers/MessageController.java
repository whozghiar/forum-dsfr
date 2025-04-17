package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Auteur;
import fr.dsfr.forum.beans.Message;
import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.beans.dto.CreerMessageDTO;
import fr.dsfr.forum.beans.dto.MessageReponseDTO;
import fr.dsfr.forum.beans.dto.ModifierMessageDTO;
import fr.dsfr.forum.exceptions.EntityNotFoundException;
import fr.dsfr.forum.services.EntityValidatorService;
import fr.dsfr.forum.services.ForumService;
import fr.dsfr.forum.services.MessageService;
import fr.dsfr.forum.services.SujetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController(value = "/messages")
@RequestMapping
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final EntityValidatorService validator;

    @GetMapping("/messages")
    public ResponseEntity<List<MessageReponseDTO>> getAllMessages() {
        List<MessageReponseDTO> dtos = messageService.getAllMessages()
                .stream()
                .map(MessageReponseDTO::convertirDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<MessageReponseDTO> getMessageById(@PathVariable Long id) {
        Message message = validator.getMessageOrThrow(id);
        return ResponseEntity.ok(MessageReponseDTO.convertirDTO(message));
    }

    @DeleteMapping("/messages/{id}/delete")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        validator.getMessageOrThrow(id);
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/forums/{forumId}/sujets/{sujetId}/messages")
    public ResponseEntity<List<MessageReponseDTO>> getMessagesBySujetOfForum(
            @PathVariable Long forumId,
            @PathVariable Long sujetId) {

        // Valide que le sujet existe et qu'il appartient bien au forum
        validator.getSujetInForumOrThrow(sujetId, forumId);

        // Récupère les messages liés à ce sujet
        List<MessageReponseDTO> dtos = messageService.getMessageBySujetId(sujetId)
                .stream()
                .map(MessageReponseDTO::convertirDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/forums/{forumId}/sujets/{sujetId}/messages/create")
    public ResponseEntity<MessageReponseDTO> createMessageInSujetOfForum(
            @PathVariable Long forumId,
            @PathVariable Long sujetId,
            @RequestBody CreerMessageDTO dto) {

        // Valide que le sujet existe et qu'il appartient bien au forum
        Sujet sujet = validator.getSujetInForumOrThrow(sujetId, forumId);

        // Valide que l'auteur existe
        Auteur auteur = validator.getAuteurOrThrow(dto.getAuteurId());

        // Crée le message à partir du DTO
        Message message = new Message();
        message.setContenu(dto.getContenu());
        message.setAuteur(auteur);
        message.setSujet(sujet);
        message.setDateCreation(LocalDateTime.now());

        Message created = messageService.createMessage(message);

        // Convertit le message créé en DTO
        MessageReponseDTO reponse = MessageReponseDTO.convertirDTO(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

    @PutMapping("/forums/{forumId}/sujets/{sujetId}/messages/{messageId}/update")
    public ResponseEntity<MessageReponseDTO> updateMessageInSujetOfForum(
            @PathVariable Long forumId,
            @PathVariable Long sujetId,
            @PathVariable Long messageId,
            @RequestBody CreerMessageDTO dto) {

        // Valide que le sujet existe et qu'il appartient bien au forum
        Sujet sujet = validator.getSujetInForumOrThrow(sujetId, forumId);

        // Valide que l'auteur existe
        Auteur auteur = validator.getAuteurOrThrow(dto.getAuteurId());

        // Valide que le message existe et qu'il appartient bien au sujet
        Message message = validator.getMessageOrThrow(messageId);

        // Met à jour le message à partir du DTO
        message.setContenu(dto.getContenu());
        message.setAuteur(auteur);
        message.setSujet(sujet);
        message.setDateCreation(LocalDateTime.now());

        Message updated = messageService.updateMessageById(messageId, message);

        // Convertit le message mis à jour en DTO
        MessageReponseDTO reponse = MessageReponseDTO.convertirDTO(updated);

        return ResponseEntity.ok(reponse);
    }

    @PatchMapping("/forums/{forumId}/sujets/{sujetId}/messages/{messageId}/patch")
    public ResponseEntity<MessageReponseDTO> patchMessageInSujetOfForum(
            @PathVariable Long forumId,
            @PathVariable Long sujetId,
            @PathVariable Long messageId,
            @RequestBody ModifierMessageDTO dto) {

        // Valide que le sujet existe et qu'il appartient bien au forum
        Sujet sujet = validator.getSujetInForumOrThrow(sujetId, forumId);

        // Valide que le message existe et qu'il appartient bien au sujet
        Message message = validator.getMessageOrThrow(messageId);

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
        MessageReponseDTO reponse = MessageReponseDTO.convertirDTO(updated);

        return ResponseEntity.ok(reponse);
    }


}
