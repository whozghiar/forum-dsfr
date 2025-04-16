package fr.dsfr.forum.services;

import fr.dsfr.forum.beans.Auteur;
import fr.dsfr.forum.beans.Forum;
import fr.dsfr.forum.beans.Message;
import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityValidatorService {

    private final ForumService forumService;
    private final SujetService sujetService;
    private final AuteurService auteurService;
    private final MessageService messageService;

    /**
     * Récupère un forum par son ID ou lève une exception si non trouvé
     * @param forumId
     * @return
     */
    public Forum getForumOrThrow(Long forumId) {
        Forum forum = forumService.getForumById(forumId);
        if (forum == null) {
            throw new EntityNotFoundException("Forum non trouvé pour l'ID: " + forumId);
        }
        return forum;
    }

    /**
     * Récupère un sujet par son ID ou lève une exception si non trouvé
     * @param sujetId
     * @return
     */
    public Sujet getSujetOrThrow(Long sujetId) {
        Sujet sujet = sujetService.getSujetById(sujetId);
        if (sujet == null) {
            throw new EntityNotFoundException("Sujet non trouvé pour l'ID: " + sujetId);
        }
        return sujet;
    }

    /**
     * Récupère un sujet dans un forum ou lève une exception si non trouvé
     * @param sujetId
     * @param forumId
     * @return
     */
    public Sujet getSujetInForumOrThrow(Long sujetId, Long forumId) {
        Sujet sujet = getSujetOrThrow(sujetId);
        if (!sujet.getForum().getId().equals(forumId)) {
            throw new EntityNotFoundException("Le sujet " + sujetId + " n'appartient pas au forum " + forumId);
        }
        return sujet;
    }

    /**
     * Récupère un auteur par son ID ou lève une exception si non trouvé
     * @param auteurId
     * @return
     */
    public Auteur getAuteurOrThrow(Long auteurId) {
        Auteur auteur = auteurService.getAuteurById(auteurId);
        if (auteur == null) {
            throw new EntityNotFoundException("Auteur non trouvé pour l'ID: " + auteurId);
        }
        return auteur;
    }

    /**
     * Récupère un message par son ID ou lève une exception si non trouvé
     * @param messageId
     * @return
     */
    public Message getMessageOrThrow(Long messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            throw new EntityNotFoundException("Message non trouvé pour l'ID: " + messageId);
        }
        return message;
    }
}