package de.lenneflow.lenneflowclient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenDto {

    private String token;

    private String description;

    private String expiration;
}
