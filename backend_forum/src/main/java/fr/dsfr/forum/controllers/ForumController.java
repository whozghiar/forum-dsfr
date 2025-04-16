package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Forum;
import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.services.EntityValidatorService;
import fr.dsfr.forum.services.ForumService;
import fr.dsfr.forum.services.SujetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/forums")
@RequestMapping
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;
    private final EntityValidatorService validator;

    @GetMapping("/forums")
    public ResponseEntity<List<Forum>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    @GetMapping("/forums/{id}")
    public ResponseEntity<Forum> getForumById(@PathVariable Long id) {
        Forum forum = validator.getForumOrThrow(id);
        return ResponseEntity.ok(forum);
    }

    @PostMapping("/forums/create")
    public ResponseEntity<Forum> createForum(@RequestBody Forum forum) {
        Forum created = forumService.createForum(forum);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/forums/{id}/update")
    public ResponseEntity<Forum> updateForum(@PathVariable Long id, @RequestBody Forum forum) {
        validator.getForumOrThrow(id);
        Forum updated = forumService.updateForumById(id, forum);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/forums/{id}")
    public ResponseEntity<Void> deleteForum(@PathVariable Long id) {
        validator.getForumOrThrow(id);
        forumService.deleteForum(id);
        return ResponseEntity.noContent().build();
    }





}
