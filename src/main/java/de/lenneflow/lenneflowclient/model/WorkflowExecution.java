package de.lenneflow.lenneflowclient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.lenneflow.lenneflowclient.enums.RunStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowExecution {

    private String runUid;

    private String workflowName;

    private String workflowDescription;

    private RunStatus runStatus;

    private List<WorkflowStepInstance> runSteps;

    private int workflowVersion;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String failureReason;

    private Map<String, Object> outputData = new HashMap<>();


}
