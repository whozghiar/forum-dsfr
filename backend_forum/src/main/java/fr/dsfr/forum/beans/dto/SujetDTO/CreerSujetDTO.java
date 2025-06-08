package fr.dsfr.forum.beans.dto.SujetDTO;

import lombok.Data;

@Data
public class CreerSujetDTO {
    private String titre;
    private Long auteurId;
    private String message;
}
