package fr.dsfr.forum.beans.dto;

import fr.dsfr.forum.beans.Message;
import lombok.Data;

@Data
public class MessageReponseDTO {
    private Long messageId;
    private Long sujetId;
    private Long auteurId;
    private String auteurPseudo;
    private String titre;
    private String contenu;
    private String dateCreation;

    /**
     * Convertit un message en MessageReponseDTO
     * @param message
     * @return
     */
    public static MessageReponseDTO convertirDTO(Message message){
        MessageReponseDTO messageReponseDTO = new MessageReponseDTO();
        messageReponseDTO.setMessageId(message.getId());
        messageReponseDTO.setSujetId(message.getSujet().getId());
        messageReponseDTO.setAuteurId(message.getAuteur().getId());
        messageReponseDTO.setAuteurPseudo(message.getAuteur().getPseudo());
        messageReponseDTO.setTitre(message.getSujet().getTitre());
        messageReponseDTO.setContenu(message.getContenu());
        messageReponseDTO.setDateCreation(message.getDateCreation().toString());
        return messageReponseDTO;
    }
}
