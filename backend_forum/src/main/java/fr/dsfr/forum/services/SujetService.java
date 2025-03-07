package fr.dsfr.forum.services;

import fr.dsfr.forum.beans.Forum;
import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.repositories.ForumRepository;
import fr.dsfr.forum.repositories.SujetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SujetService {

    private final SujetRepository sujetRepository;
    private final ForumRepository forumRepository;


    /**
     * Crée un sujet
     * @param sujet
     * @return
     */
    public Sujet createSujet(Sujet sujet) {
        return sujetRepository.save(sujet);
    }

    /**
     * Crée un sujet dans un forum
     * @param forumId
     * @param sujet
     */
    public Sujet createSujet(Long forumId, Sujet sujet) {
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
        Sujet sujetToUpdate = sujetRepository.findById(id).orElse(null);
        if (sujetToUpdate != null) {
            sujetToUpdate.setTitre(sujet.getTitre());
            sujetToUpdate.setMessages(sujet.getMessages());
            return sujetRepository.save(sujetToUpdate);
        }
        return null;
    }

    /**
     * Supprime un sujet
     * @param id
     */
    public void deleteSujet(Long id) {
        sujetRepository.deleteById(id);
    }

    /**
     * Récupère un sujet par son id
     * @param id
     * @return
     */
    public Sujet getSujetById(Long id) {
        return sujetRepository.findById(id).orElse(null);
    }

    /**
     * Récupère tous les sujets d'un forum
     * @param forumId
     * @return
     */
    public List<Sujet> getSujetByForumId(Long forumId) {
        return sujetRepository.findByForumId(forumId);
    }

    /**
     * Récupère tous les sujets contenant un titre
     * @param titre
     * @return
     */
    public List<Sujet> getSujetByTitreContaining(String titre) {
        return sujetRepository.findByTitreContaining(titre);
    }

    /**
     * Récupère tous les sujets
     * @return
     */
    public List<Sujet> getAllSujets() {
        return sujetRepository.findAll();
    }


}
