package fr.dsfr.forum.beans.dto.AuthDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReponseLoginDTO {
    private String accessToken;
    private String refreshToken;
    private String idToken;
}
