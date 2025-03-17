package de.lenneflow.lenneflowclient.model;

import de.lenneflow.lenneflowclient.enums.CloudProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NodeGroupDTO {

    private String clusterUid;

    private String description;

    private int minimumNodeCount;

    private int maximumNodeCount;

    private int desiredNodeCount;

    private String clusterName;

    private String region;

    private CloudProvider cloudProvider;

}
