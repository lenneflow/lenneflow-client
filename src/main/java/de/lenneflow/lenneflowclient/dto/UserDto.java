package de.lenneflow.lenneflowclient.dto;

import de.lenneflow.lenneflowclient.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String username;

    private String email;

    private String password;

    private Set<Role> authorities;

}
