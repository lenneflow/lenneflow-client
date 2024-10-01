package de.lenneflow.lenneflowclient.dto;

import de.lenneflow.lenneflowclient.enums.ControlStructure;
import de.lenneflow.lenneflowclient.model.DecisionCase;
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
public class SwitchWorkflowStep {


    private String uid;

    private String name;

    private String workflowUid;

    private String description;

    private Integer retryCount = 0;

    private ControlStructure controlStructure = ControlStructure.SWITCH;

    private int executionOrder;

    private String switchCondition; //example {stepname.outputData.field.field} > 10 ; will be validated by creation

    private List<DecisionCase> decisionCases = new ArrayList<>();

    private Map<String, Object> inputData = new LinkedHashMap<>();

    private LocalDateTime creationTime;

    private LocalDateTime updateTime;

}
