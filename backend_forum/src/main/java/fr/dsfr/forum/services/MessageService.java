package fr.dsfr.forum.services;

import fr.dsfr.forum.beans.Message;
import fr.dsfr.forum.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    /**
     * Crée un message
     * @param message
     * @return
     */
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    /**
     * Met à jour un message par son id
     * @param id
     * @return
     */
    public Message updateMessageById(Long id){
        Message messageToUpdate = messageRepository.findById(id).orElse(null);
        if (messageToUpdate != null) {
            return messageRepository.save(messageToUpdate);
        }
        return null;
    }

    /**
     * Supprime un message
     * @param id
     */
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    /**
     * Récupère un message par son id
     * @param id
     * @return
     */
    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    /**
     * Récupère tous les messages d'un sujet
     * @param sujetId
     * @return
     */
    public List<Message> getMessageBySujetId(Long sujetId) {
        return messageRepository.findBySujetId(sujetId);
    }

    /**
     * Récupère tous les messages d'un auteur
     * @param auteurId
     * @return
     */
    public List<Message> getMessageByAuteurId(Long auteurId) {
        return messageRepository.findByAuteurId(auteurId);
    }

    /**
     * Récupère tous les messages
     * @return
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
