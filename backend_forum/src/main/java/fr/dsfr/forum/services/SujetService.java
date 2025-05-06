package fr.dsfr.forum.services;

import fr.dsfr.forum.beans.Forum;
import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.repositories.ForumRepository;
import fr.dsfr.forum.repositories.SujetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SujetService {

    private final SujetRepository sujetRepository;
    private final ForumRepository forumRepository;


    /**
     * Crée un sujet
     * @param sujet
     * @return
     */
    public Sujet createSujet(Sujet sujet) {
        log.info("Création d'un sujet : {}", sujet);
        return sujetRepository.save(sujet);
    }

    /**
     * Crée un sujet dans un forum
     * @param forumId
     * @param sujet
     */
    public Sujet createSujet(Long forumId, Sujet sujet) {
        log.info("Création d'un sujet dans le forum ID {} : {}", forumId, sujet);
        Forum forum = forumRepository.findById(forumId).orElse(null);
        if (forum != null) {
            sujet.setForum(forum);
        }
        return sujetRepository.save(sujet);
    }


    /**
     * Met à jour un sujet par son id
     * @param id
     * @param sujet
     * @return
     */
    public Sujet updateSujetById(Long id, Sujet sujet) {
        log.info("Mise à jour du sujet ID {} avec données : {}", id, sujet);
        Sujet sujetToUpdate = sujetRepository.findById(id).orElse(null);
        if (sujetToUpdate != null) {
            sujetToUpdate.setTitre(sujet.getTitre());
            sujetToUpdate.setMessages(sujet.getMessages());
            sujetToUpdate.setEpingle(sujet.getEpingle());
            return sujetRepository.save(sujetToUpdate);
        }
        log.warn("Sujet ID {} non trouvé pour mise à jour", id);
        return null;
    }

    /**
     * Supprime un sujet
     * @param id
     */
    public void deleteSujet(Long id) {
        log.info("Suppression du sujet ID {}", id);
        sujetRepository.deleteById(id);
    }

    /**
     * Récupère un sujet par son id
     * @param id
     * @return
     */
    public Sujet getSujetById(Long id) {
        log.info("Recherche du sujet par ID {}", id);
        return sujetRepository.findById(id).orElse(null);
    }

    /**
     * Récupère tous les sujets d'un forum
     * @param forumId
     * @return
     */
    public List<Sujet> getSujetByForumId(Long forumId) {
        log.info("Récupération des sujets du forum ID {}", forumId);
        return sujetRepository.findByForumId(forumId);
    }

    /**
     * Récupère tous les sujets contenant un titre
     * @param titre
     * @return
     */
    public List<Sujet> getSujetByTitreContaining(String titre) {
        log.info("Recherche de sujets contenant le titre '{}'", titre);
        return sujetRepository.findByTitreContaining(titre);
    }

    /**
     * Récupère tous les sujets
     * @return
     */
    public List<Sujet> getAllSujets() {
        log.info("Récupération de tous les sujets");
        return sujetRepository.findAll();
    }


}
