package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.services.EntityValidatorService;
import fr.dsfr.forum.services.SujetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/sujets")
@RequestMapping
@RequiredArgsConstructor
public class SujetController {

    private final SujetService sujetService;
    private final EntityValidatorService validator;

    @GetMapping("/sujets")
    public ResponseEntity<List<Sujet>> getAllSujets() {
        return ResponseEntity.ok(sujetService.getAllSujets());
    }

    @GetMapping("/sujets/{id}")
    public ResponseEntity<Sujet> getSujetById(@PathVariable Long id) {
        Sujet sujet = validator.getSujetOrThrow(id);
        return ResponseEntity.ok(sujet);
    }

    @PostMapping("/sujets/create")
    public ResponseEntity<Sujet> createSujet(@RequestBody Sujet sujet) {
        Sujet created = sujetService.createSujet(sujet);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/sujets/{id}/update")
    public ResponseEntity<Sujet> updateSujet(@PathVariable Long id, @RequestBody Sujet sujet) {
        validator.getSujetOrThrow(id);
        Sujet updated = sujetService.updateSujetById(id, sujet);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/sujets/{id}/delete")
    public ResponseEntity<Void> deleteSujet(@PathVariable Long id) {
        validator.getSujetOrThrow(id);
        sujetService.deleteSujet(id);
        return ResponseEntity.noContent().build();
    }
}
