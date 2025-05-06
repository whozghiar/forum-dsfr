package fr.dsfr.forum.beans.dto.AuteurDTO;

import fr.dsfr.forum.beans.Auteur;
import fr.dsfr.forum.beans.dto.MessageDTO.MessageReponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class AuteurDetailReponseDTO {
    private Long auteurId;
    private String pseudo;
    private List<MessageReponseDTO> messages;
    private Integer nbMessages;

    public static AuteurDetailReponseDTO convertir(Auteur auteur) {
        AuteurDetailReponseDTO dto = new AuteurDetailReponseDTO();
        dto.setAuteurId(auteur.getId());
        dto.setPseudo(auteur.getPseudo());
        return dto;
    }
}
