package fr.dsfr.forum.services;

import fr.dsfr.forum.beans.Auteur;
import fr.dsfr.forum.beans.dto.AuteurDTO.AuteurReponseDTO;
import fr.dsfr.forum.beans.dto.MessageDTO.MessageReponseDTO;
import fr.dsfr.forum.repositories.AuteurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuteurService {

    /**
     * Service de gestion des messages
     */
    private final MessageService messageService;

    private final AuteurRepository auteurRepository;

    /**
     * Crée un auteur
     * @param auteur
     * @return
     */
    public Auteur createAuteur(Auteur auteur) {
        log.info("Création d'un auteur : {}", auteur);
        return auteurRepository.save(auteur);
    }

    /**
     * Met à jour un auteur par son id
     * @param id
     * @param auteur
     * @return
     */
    public Auteur updateAuteurById(Long id, Auteur auteur) {
        log.info("Mise à jour de l'auteur ID {} avec données : {}", id, auteur);
        Auteur auteurToUpdate = auteurRepository.findById(id).orElse(null);
        if (auteurToUpdate != null) {
            auteurToUpdate.setPseudo(auteur.getPseudo());
            return auteurRepository.save(auteurToUpdate);
        }
        log.warn("Auteur ID {} non trouvé pour mise à jour", id);
        return null;
    }

    /**
     * Supprime un auteur
     * @param id
     */
    public void deleteAuteur(Long id) {
        log.info("Suppression de l'auteur ID {}", id);
        auteurRepository.deleteById(id);
    }

    /**
     * Récupère un auteur par son id
     * @param id
     * @return
     */
    public Auteur getAuteurById(Long id) {
        log.info("Recherche de l'auteur par ID {}", id);
        return auteurRepository.findById(id).orElse(null);
    }

    /**
     * Récupère un auteur par son pseudo
     * @param pseudo
     * @return
     */
    public Auteur getAuteurByPseudo(String pseudo) {
        log.info("Recherche de l'auteur par pseudo '{}'", pseudo);
        return auteurRepository.findByPseudo(pseudo).orElse(null);
    }

    /**
     * Récupère des auteurs par un filtre sur le pseudo
     * @param pseudo
     * @return
     */
    public List<Auteur> getAuteurByPseudoContaining(String pseudo) {
        log.info("Recherche d'auteurs contenant le pseudo '{}'", pseudo);
        return auteurRepository.findByPseudoContaining(pseudo);
    }

    /**
     * Récupère tous les auteurs
     * @return
     */
    public List<Auteur> getAllAuteurs() {
        log.info("Récupération de tous les auteurs");
        return auteurRepository.findAll();
    }


    // ----------- MÉTHODES DE PROJECTION -------------- //
    /**
     * Enrichit les informations de l'auteur avec les messages associés ainsi que le nombre de messages.
     * @param {@link Auteur} auteur
     * @return {@link AuteurReponseDTO}
     */
    public AuteurReponseDTO enrichirAuteurDTO(Auteur auteur) {
        log.info("Enrichissement de l'auteur ID {} avec les messages associés", auteur.getId());
        AuteurReponseDTO dto = AuteurReponseDTO.convertir(auteur);
        var messages = messageService.getMessageByAuteurId(dto.getAuteurId())
                .stream()
                .map(MessageReponseDTO::convertir)
                .toList();

        dto.setMessages(messages);
        dto.setNbMessages(messages.size());

        return dto;
    }
}
