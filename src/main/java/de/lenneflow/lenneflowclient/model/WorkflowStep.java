package de.lenneflow.lenneflowclient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.lenneflow.lenneflowclient.enums.ControlStructure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowStep {

    private String uid;

    private String name;

    private String workflowUid;

    private String workflowName;

    private String description;

    private ControlStructure controlStructure;

    private int executionOrder;

    private String functionUid;

    private String subWorkflowUid;

    private List<DecisionCase> decisionCases = new ArrayList<>();

    private String switchCase;

    private String stopCondition;

    private Map<String, Object> inputData = new LinkedHashMap<>();

    private Integer retryCount = 0;

    @JsonIgnore
    private LocalDateTime created;

    @JsonIgnore
    private LocalDateTime updated;

    @JsonIgnore
    private String stringInputData;

}
