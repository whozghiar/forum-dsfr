package fr.dsfr.forum.beans.dto.MessageDTO;

import lombok.Data;

@Data
public class ModifierMessageDTO {
    private String contenu;
    private Long sujetId;
    private Long auteurId;
}
