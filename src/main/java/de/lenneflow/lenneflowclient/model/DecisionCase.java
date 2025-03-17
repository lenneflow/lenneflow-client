package de.lenneflow.lenneflowclient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DecisionCase {

    private String name;

    private String functionUid;

    private String subWorkflowUid;

    private Map<String, Object> inputData = new LinkedHashMap<>();

    private Integer retryCount;

    @JsonIgnore
    private String stringInputData;
}
