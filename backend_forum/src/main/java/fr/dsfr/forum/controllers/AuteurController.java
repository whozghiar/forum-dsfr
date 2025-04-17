package fr.dsfr.forum.controllers;

import fr.dsfr.forum.beans.Auteur;
import fr.dsfr.forum.beans.dto.AuteurDTO.AuteurReponseDTO;
import fr.dsfr.forum.beans.dto.AuteurDTO.CreerAuteurDTO;
import fr.dsfr.forum.beans.dto.AuteurDTO.ModifierAuteurDTO;
import fr.dsfr.forum.beans.dto.MessageDTO.MessageReponseDTO;
import fr.dsfr.forum.services.AuteurService;
import fr.dsfr.forum.services.EntityValidatorService;
import fr.dsfr.forum.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/auteurs")
@RequiredArgsConstructor
public class AuteurController {

    private final AuteurService auteurService;
    private final MessageService messageService;
    private final EntityValidatorService validator;

    @GetMapping
    public ResponseEntity<List<AuteurReponseDTO>> getAllAuteurs() {
        List<Auteur> auteurs = auteurService.getAllAuteurs();
        List<AuteurReponseDTO> dtos = auteurs.stream()
                .map(auteurService::enrichirAuteurDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{idAuteur}")
    public ResponseEntity<AuteurReponseDTO> getAuteurById(@PathVariable Long idAuteur) {
        Auteur auteur = validator.getAuteurOrThrow(idAuteur);

        AuteurReponseDTO dto = AuteurReponseDTO.convertir(auteur);

        // On récupère les messages de l'auteur
        dto.setMessages(
                messageService.getMessageByAuteurId(dto.getAuteurId()).stream()
                        .map(MessageReponseDTO::convertir)
                        .toList()
        );
        dto.setNbMessages(dto.getMessages().size());

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<AuteurReponseDTO> createAuteur(@RequestBody CreerAuteurDTO dto) {
        Auteur auteur = new Auteur();
        auteur.setPseudo(dto.getPseudo());

        Auteur created = auteurService.createAuteur(auteur);
        return ResponseEntity.status(HttpStatus.CREATED).body(AuteurReponseDTO.convertir(created));
    }

    @PutMapping("/{idAuteur}/update")
    public ResponseEntity<AuteurReponseDTO> updateAuteur(
            @PathVariable Long idAuteur,
            @RequestBody CreerAuteurDTO dto) {

        validator.getAuteurOrThrow(idAuteur);

        Auteur auteur = new Auteur();
        auteur.setPseudo(dto.getPseudo());

        Auteur updated = auteurService.updateAuteurById(idAuteur, auteur);
        return ResponseEntity.ok(AuteurReponseDTO.convertir(updated));
    }

    @PatchMapping("/{auteurId}/patch")
    public ResponseEntity<AuteurReponseDTO> patchAuteur(
            @PathVariable Long auteurId,
            @RequestBody ModifierAuteurDTO dto) {

        Auteur auteur = validator.getAuteurOrThrow(auteurId);

        if (dto.getPseudo() != null) {
            auteur.setPseudo(dto.getPseudo());
        }

        Auteur updated = auteurService.updateAuteurById(auteurId, auteur);
        return ResponseEntity.ok(AuteurReponseDTO.convertir(updated));
    }
    @DeleteMapping("/{auteurId}/delete")
    public ResponseEntity<Void> deleteAuteur(@PathVariable Long auteurId) {
        validator.getAuteurOrThrow(auteurId);
        auteurService.deleteAuteur(auteurId);
        return ResponseEntity.noContent().build();
    }


}
