package de.lenneflow.lenneflowclient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Workflow {

    private String uid;

    private String name;

    private String description;

    private List<WorkflowStep> steps = new LinkedList<>();

    private boolean statusListenerEnabled = false;

    private boolean restartable = true;

    private long timeOutInSeconds = Long.MAX_VALUE;

    private JsonSchema inputDataSchema;

    private JsonSchema outputDataSchema;

    @JsonIgnore
    private LocalDateTime created;

    @JsonIgnore
    private LocalDateTime updated;

}
