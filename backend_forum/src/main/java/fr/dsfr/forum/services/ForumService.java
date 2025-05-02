package fr.dsfr.forum.services;

import fr.dsfr.forum.beans.Forum;
import fr.dsfr.forum.repositories.ForumRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ForumService {
    private final ForumRepository forumRepository;

    /**
     * Crée un forum
     * @param forum
     * @return
     */
    public Forum createForum(Forum forum) {
        log.info("Création d'un forum : {}", forum);
        return forumRepository.save(forum);
    }

    /**
     * Met à jour un forum par son id
     * @param id
     * @param forum
     * @return
     */
    public Forum updateForumById(Long id, Forum forum) {
        log.info("Mise à jour du forum ID {} avec données : {}", id, forum);
        Forum forumToUpdate = forumRepository.findById(id).orElse(null);
        if (forumToUpdate != null) {
            forumToUpdate.setTitre(forum.getTitre());
            forumToUpdate.setDescription(forum.getDescription());
            forumToUpdate.setSujets(forum.getSujets());
            return forumRepository.save(forumToUpdate);
        }
        log.warn("Forum ID {} non trouvé pour mise à jour", id);
        return null;
    }

    /**
     * Supprime un forum
     * @param id
     * @return
     */
    public void deleteForum(Long id) {
        log.info("Suppression du forum ID {}", id);
        forumRepository.deleteById(id);
    }

    /**
     * Récupère tous les forums
     * @return
     */
    public List<Forum> getAllForums() {
        log.info("Récupération de tous les forums");
        return forumRepository.findAll();
    }

    /**
     * Récupère un forum par son id
     * @param id
     * @return
     */
    public Forum getForumById(Long id) {
        log.info("Recherche du forum par ID {}", id);
        return forumRepository.findById(id).orElse(null);
    }



}
