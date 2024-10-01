package de.lenneflow.lenneflowclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleWorkflowStep {

    private String uid;

    private String name;

    private String workflowUid;

    private String description;

    private int executionOrder;

    private Integer retryCount = 0;

    private String functionId;

    private Map<String, Object> inputData = new LinkedHashMap<>();

    private LocalDateTime creationTime;

    private LocalDateTime updateTime;
}
