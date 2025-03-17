package de.lenneflow.lenneflowclient.dto;

import de.lenneflow.lenneflowclient.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto2 {

    private String username;

    private String email;

    private String password;

    private String password2;

    private Role role;

}
