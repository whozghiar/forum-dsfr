package fr.dsfr.forum.beans.dto;

import lombok.Data;

@Data
public class ModifierMessageDTO {
    private String contenu;
    private Long sujetId;
    private Long auteurId;
}
