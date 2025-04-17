package fr.dsfr.forum.beans.dto.ForumDTO;

import fr.dsfr.forum.beans.Forum;
import fr.dsfr.forum.beans.dto.SujetDTO.SujetReponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class ForumReponseDTO {
    private Long idForum;
    private String titre;
    private String description;
    private List<SujetReponseDTO> sujets;

    public static ForumReponseDTO convertir(Forum forum){
        ForumReponseDTO dto = new ForumReponseDTO();
        dto.setIdForum(forum.getId());
        dto.setTitre(forum.getTitre());
        dto.setDescription(forum.getDescription());
        dto.setSujets(forum.getSujets().stream()
                .map(SujetReponseDTO::convertir)
                .toList());
        return dto;
    }
}
