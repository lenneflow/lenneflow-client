package de.lenneflow.lenneflowclient.model;


import de.lenneflow.lenneflowclient.enums.CloudProvider;
import de.lenneflow.lenneflowclient.enums.ClusterStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KubernetesCluster {

    private String uid;

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

    private String apiServerEndpoint;

    private String caCertificate;

    private List<String> supportedFunctionTypes = new ArrayList<>();

    private ClusterStatus status;

    private String cloudCredentialUid;

    private List<Integer> usedHostPorts = new ArrayList<>();

    private String kubernetesAccessTokenUid;

    private String ingressServiceName;

    private String serviceUser;

    private String hostUrl;

    private LocalDateTime created;

    private LocalDateTime updated;

    private boolean managed;

}
