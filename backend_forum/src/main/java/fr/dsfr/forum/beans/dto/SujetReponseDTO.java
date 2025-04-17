package fr.dsfr.forum.beans.dto;

import fr.dsfr.forum.beans.Sujet;
import lombok.Data;

import java.util.List;

@Data
public class SujetReponseDTO {
    private Long idSujet;
    private Long idForum;
    private String titre;
    private List<MessageReponseDTO> messages;


    public static SujetReponseDTO convertir(Sujet sujet){
        SujetReponseDTO sujetReponseDTO = new SujetReponseDTO();
        sujetReponseDTO.setIdSujet(sujet.getId());
        sujetReponseDTO.setTitre(sujet.getTitre());
        sujetReponseDTO.setIdForum(sujet.getForum().getId());

        // Convertir les messages en DTO et les ajouter à la liste
        List<MessageReponseDTO> messageReponseDTOs = sujet.getMessages().stream()
                .map(MessageReponseDTO::convertirDTO)
                .toList();

        sujetReponseDTO.setMessages(messageReponseDTOs);
        return sujetReponseDTO;
    }
}
