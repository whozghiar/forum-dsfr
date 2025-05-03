package fr.dsfr.forum.beans.dto.SujetDTO;

import fr.dsfr.forum.beans.Sujet;
import fr.dsfr.forum.beans.Message;
import fr.dsfr.forum.beans.dto.AuteurDTO.AuteurReponseDTO;
import fr.dsfr.forum.beans.dto.MessageDTO.MessageReponseDTO;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class SujetReponseDTO {
    private Long idSujet;
    private Long idForum;
    private String titre;
    private AuteurReponseDTO auteur;
    private Integer nbMessages;
    private String dateCreation;


    public static SujetReponseDTO convertir(Sujet sujet){
        SujetReponseDTO sujetReponseDTO = new SujetReponseDTO();
        sujetReponseDTO.setIdSujet(sujet.getId());
        sujetReponseDTO.setTitre(sujet.getTitre());
        sujetReponseDTO.setIdForum(sujet.getForum().getId());

        // Convertir les messages en DTO et les ajouter à la liste
        List<MessageReponseDTO> messageReponseDTOs = sujet.getMessages().stream()
                .map(MessageReponseDTO::convertir)
                .toList();

        // récupérer la longueur de messages
        sujetReponseDTO.setNbMessages(messageReponseDTOs.size());

        // Déterminer l'auteur à partir du premier message
        if (!sujet.getMessages().isEmpty()) {
            Message premier = sujet.getMessages().get(0);
            sujetReponseDTO.setAuteur(AuteurReponseDTO.convertir(premier.getAuteur()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String dateFormatee = premier.getDateCreation().format(formatter);
            sujetReponseDTO.setDateCreation(dateFormatee);
        }

        return sujetReponseDTO;
    }
}
