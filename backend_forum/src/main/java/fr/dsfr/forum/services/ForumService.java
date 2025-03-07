package fr.dsfr.forum.services;

import fr.dsfr.forum.beans.Forum;
import fr.dsfr.forum.repositories.ForumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ForumService {
    private final ForumRepository forumRepository;

    /**
     * Crée un forum
     * @param forum
     * @return
     */
    public Forum createForum(Forum forum) {
        return forumRepository.save(forum);
    }

    /**
     * Met à jour un forum par son id
     * @param id
     * @param forum
     * @return
     */
    public Forum updateForumById(Long id, Forum forum) {
        Forum forumToUpdate = forumRepository.findById(id).orElse(null);
        if (forumToUpdate != null) {
            forumToUpdate.setTitre(forum.getTitre());
            forumToUpdate.setDescription(forum.getDescription());
            forumToUpdate.setSujets(forum.getSujets());
            return forumRepository.save(forumToUpdate);
        }
        return null;
    }

    /**
     * Supprime un forum
     * @param id
     * @return
     */
    public void deleteForum(Long id) {
        forumRepository.deleteById(id);
    }

    /**
     * Récupère tous les forums
     * @return
     */
    public List<Forum> getAllForums() {
        return forumRepository.findAll();
    }

    /**
     * Récupère un forum par son id
     * @param id
     * @return
     */
    public Forum getForumById(Long id) {
        return forumRepository.findById(id).orElse(null);
    }



}
