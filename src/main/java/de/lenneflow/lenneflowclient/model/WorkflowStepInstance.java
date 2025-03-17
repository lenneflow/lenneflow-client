package de.lenneflow.lenneflowclient.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import de.lenneflow.lenneflowclient.enums.ControlStructure;
import de.lenneflow.lenneflowclient.enums.RunOrderLabel;
import de.lenneflow.lenneflowclient.enums.RunStatus;
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
public class WorkflowStepInstance{

    private String uid;

    private String name;

    private String workflowUid;

    private String workflowInstanceUid;

    private String workflowName;

    private String description;

    private ControlStructure controlStructure;

    private int executionOrder;

    private RunStatus runStatus;

    private String functionUid;

    private String subWorkflowUid;

    private List<DecisionCase> decisionCases = new ArrayList<>();

    private String selectedCaseName;

    private String switchCase;

    private String stopCondition;

    private String nextStepId;

    private String previousStepId;

    private RunOrderLabel runOrderLabel;

    private Integer retryCount = 0;

    private Integer runCount = 0;

    private LocalDateTime created;

    private LocalDateTime updated;

    private String failureReason;

    @JsonIgnore
    private LocalDateTime scheduledTime;

    @JsonIgnore
    private LocalDateTime startTime;

    @JsonIgnore
    private LocalDateTime endTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    private Map<String, Object> inputData = new LinkedHashMap<>();

    private Map<String, Object> outputData = new LinkedHashMap<>();

}
