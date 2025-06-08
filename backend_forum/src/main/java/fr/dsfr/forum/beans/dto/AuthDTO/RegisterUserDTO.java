package fr.dsfr.forum.beans.dto.AuthDTO;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String username;
    private String email;
    private String password;
}
