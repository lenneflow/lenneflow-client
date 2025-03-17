package de.lenneflow.lenneflowclient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.lenneflow.lenneflowclient.enums.JsonSchemaVersion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonSchema {

    private String uid;

    private String name;

    private String description;

    private String schema;

    private JsonSchemaVersion schemaVersion;

    @JsonIgnore
    private LocalDateTime created;

    @JsonIgnore
    private LocalDateTime updated;

}
