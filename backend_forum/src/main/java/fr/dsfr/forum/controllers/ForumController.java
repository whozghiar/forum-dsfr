package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.services.ForumService;
import fr.dsfr.forum.services.SujetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;
    private final SujetService sujetService;


    @PostMapping("/{forumId}/sujets")
    public Sujet createSujet(@PathVariable Long forumId, @RequestBody Sujet sujet) {
        return sujetService.createSujet(forumId, sujet);
    }
}
