package fr.dsfr.forum.beans.dto.AuthDTO;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}
