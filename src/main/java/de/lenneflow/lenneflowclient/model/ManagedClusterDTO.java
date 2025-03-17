package de.lenneflow.lenneflowclient.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import de.lenneflow.lenneflowclient.enums.CloudProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManagedClusterDTO {

    private String clusterName;

    private String region;

    private String description;

    private String kubernetesVersion;

    private CloudProvider cloudProvider;

    private int desiredNodeCount;

    private int minimumNodeCount;

    private int maximumNodeCount;

    private String instanceType;

    private String amiType;

    private List<String> supportedFunctionTypes = new ArrayList<>();

    @JsonIgnore
    private String stringFunctionTypes;

    private String cloudCredentialUid;

}
