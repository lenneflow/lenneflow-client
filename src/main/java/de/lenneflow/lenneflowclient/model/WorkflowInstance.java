package de.lenneflow.lenneflowclient.model;


import de.lenneflow.lenneflowclient.enums.RunStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * DB entity for workflow instance
 *
 * @author Idrissa Ganemtore
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowInstance {

    private String uid;

    private String workflowUid;

    private String parentInstanceUid = null;

    private String parentStepInstanceUid = null;

    private String name;

    private RunStatus runStatus;

    private String description;

    private List<WorkflowStepInstance> stepInstances = new LinkedList<>();

    private boolean statusListenerEnabled = false;

    private boolean restartable = true;

    private long timeOutInSeconds = Long.MAX_VALUE;

    private String failureReason;

    private LocalDateTime created;

    private Map<String, Object> inputData = new LinkedHashMap<>();

    private Map<String, Object> outputData = new LinkedHashMap<>();

    private LocalDateTime updated;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
