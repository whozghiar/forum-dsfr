package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Message;
import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.exceptions.EntityNotFoundException;
import fr.dsfr.forum.services.EntityValidatorService;
import fr.dsfr.forum.services.ForumService;
import fr.dsfr.forum.services.MessageService;
import fr.dsfr.forum.services.SujetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/messages")
@RequestMapping
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final EntityValidatorService validator;

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = validator.getMessageOrThrow(id);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/messages/create")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message created = messageService.createMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/messages/{id}/update")
    public ResponseEntity<Message> updateMessage(@PathVariable Long id,@RequestBody Message message) {
        validator.getMessageOrThrow(id);
        Message updated = messageService.updateMessageById(id, message);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/messages/{id}/delete")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        validator.getMessageOrThrow(id);
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/forums/{forumId}/sujets/{sujetId}/messages")
    public ResponseEntity<List<Message>> getMessagesBySujetOfForum(
            @PathVariable Long forumId,
            @PathVariable Long sujetId) {

        // Valide que le sujet existe et qu'il appartient bien au forum
        validator.getSujetInForumOrThrow(sujetId, forumId);

        // Récupère les messages liés à ce sujet
        List<Message> messages = messageService.getMessageBySujetId(sujetId);

        return ResponseEntity.ok(messages);
    }
}
