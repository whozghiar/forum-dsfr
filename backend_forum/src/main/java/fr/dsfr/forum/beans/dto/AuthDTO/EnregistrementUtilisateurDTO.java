package fr.dsfr.forum.beans.dto.AuthDTO;

import lombok.Data;

@Data
public class EnregistrementUtilisateurDTO {
    private String pseudonyme;
    private String email;
    private String motDePasse;
}
