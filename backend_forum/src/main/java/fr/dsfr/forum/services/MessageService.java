package fr.dsfr.forum.services;

import fr.dsfr.forum.beans.Message;
import fr.dsfr.forum.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;

    /**
     * Crée un message
     * @param message
     * @return
     */
    public Message createMessage(Message message) {
        log.info("Création d'un message : {}", message);
        return messageRepository.save(message);
    }

    /**
     * Met à jour un message par son id
     * @param id
     * @return
     */
    public Message updateMessageById(Long id, Message message) {
        log.info("Mise à jour du message ID {} avec données : {}", id, message);
        Message messageToUpdate = messageRepository.findById(id).orElse(null);
        if (messageToUpdate != null) {
            messageToUpdate.setContenu(message.getContenu());
            messageToUpdate.setDateCreation(message.getDateCreation());
            messageToUpdate.setSujet(message.getSujet());
            messageToUpdate.setAuteur(message.getAuteur());
            return messageRepository.save(messageToUpdate);
        }
        log.warn("Message ID {} non trouvé pour mise à jour", id);
        return null;
    }

    /**
     * Supprime un message
     * @param id
     */
    public void deleteMessage(Long id) {
        log.info("Suppression du message ID {}", id);
        messageRepository.deleteById(id);
    }

    /**
     * Récupère un message par son id
     * @param id
     * @return
     */
    public Message getMessageById(Long id) {
        log.info("Récupération du message ID {}", id);
        return messageRepository.findById(id).orElse(null);
    }

    /**
     * Récupère tous les messages d'un sujet
     * @param sujetId
     * @return
     */
    public List<Message> getMessageBySujetId(Long sujetId) {
        log.info("Récupération des messages du sujet ID {}", sujetId);
        return messageRepository.findBySujetId(sujetId);
    }

    /**
     * Récupère tous les messages d'un auteur
     * @param auteurId
     * @return
     */
    public List<Message> getMessageByAuteurId(Long auteurId) {
        log.info("Récupération des messages de l'auteur ID {}", auteurId);
        return messageRepository.findByAuteurId(auteurId);
    }

    /**
     * Récupère tous les messages
     * @return
     */
    public List<Message> getAllMessages() {
        log.info("Récupération de tous les messages");
        return messageRepository.findAll();
    }
}
