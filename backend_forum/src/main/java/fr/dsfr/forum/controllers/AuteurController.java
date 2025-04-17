package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Auteur;
import fr.dsfr.forum.services.AuteurService;
import fr.dsfr.forum.services.EntityValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/auteurs")
@RequestMapping
@RequiredArgsConstructor
public class AuteurController {

    private final AuteurService auteurService;
    private final EntityValidatorService validator;

    @GetMapping("/auteurs")
    public ResponseEntity<List<Auteur>> getAllAuteurs() {
        return ResponseEntity.ok(auteurService.getAllAuteurs());
    }

    @GetMapping("/auteurs/{id}")
    public ResponseEntity<Auteur> getAuteurById(@PathVariable Long id) {
        Auteur auteur = validator.getAuteurOrThrow(id);
        return ResponseEntity.ok(auteur);
    }

    @PostMapping("/auteurs/create")
    public ResponseEntity<Auteur> createAuteur(@RequestBody Auteur auteur) {
        Auteur created = auteurService.createAuteur(auteur);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/auteurs/{id}/update")
    public ResponseEntity<Auteur> updateAuteur(@PathVariable Long id,@RequestBody Auteur auteur) {
        validator.getAuteurOrThrow(id);
        Auteur updated = auteurService.updateAuteurById(id, auteur);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/auteurs/{id}/delete")
    public ResponseEntity<Void> deleteAuteur(@PathVariable Long id) {
        validator.getAuteurOrThrow(id);
        auteurService.deleteAuteur(id);
        return ResponseEntity.noContent().build();
    }


}
