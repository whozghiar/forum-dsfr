package fr.dsfr.forum.beans.dto.SujetDTO;

import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.beans.dto.MessageDTO.MessageReponseDTO;
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
                .map(MessageReponseDTO::convertir)
                .toList();

        sujetReponseDTO.setMessages(messageReponseDTOs);
        return sujetReponseDTO;
    }
}
