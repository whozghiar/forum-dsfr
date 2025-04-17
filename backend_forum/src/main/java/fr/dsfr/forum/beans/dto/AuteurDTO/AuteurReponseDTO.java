package fr.dsfr.forum.beans.dto.AuteurDTO;

import fr.dsfr.forum.beans.Auteur;
import fr.dsfr.forum.beans.dto.MessageDTO.MessageReponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class AuteurReponseDTO {
    private Long auteurId;
    private String pseudo;
    private List<MessageReponseDTO> messages;
    private Integer nbMessages;

    public static AuteurReponseDTO convertir(Auteur auteur) {
        AuteurReponseDTO dto = new AuteurReponseDTO();
        dto.setAuteurId(auteur.getId());
        dto.setPseudo(auteur.getPseudo());
        return dto;
    }

}
