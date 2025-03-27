package de.lenneflow.lenneflowclient.model;


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
public class UserDto{

    private String firstname;

    private String lastname;

    private String username;

    private String email;

    private String password;

    private Set<Role> authorities;

}
