package de.lenneflow.lenneflowclient.model;

import de.lenneflow.lenneflowclient.enums.RunStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowExecution {

    private String runId;

    private String workflowInstanceId;

    private String workflowName;

    private String workflowDescription;

    private RunStatus runStatus;

    private List<WorkflowStepInstance> runSteps;

    private int workflowVersion;

    private LocalDateTime startTime;

    private String stringStartTime;

    private LocalDateTime endTime;

    private String stringEndTime;

    private Duration duration;

    private String errors;

    private Map<String, Object> runOutput = new HashMap<>();


}
