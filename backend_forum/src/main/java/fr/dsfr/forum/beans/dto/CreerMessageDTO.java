package fr.dsfr.forum.beans.dto;

import lombok.Data;

@Data
public class CreerMessageDTO {
    private Long auteurId;
    private String contenu;
}
