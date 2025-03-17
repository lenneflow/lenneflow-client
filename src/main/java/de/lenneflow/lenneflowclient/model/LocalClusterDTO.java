package de.lenneflow.lenneflowclient.model;

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
public class LocalClusterDTO {

    private String clusterName;

    private String description;

    private List<String> supportedFunctionTypes = new ArrayList<>();

    private String apiServerEndpoint;

    private String caCertificate;

    private String kubernetesAccessTokenUid;

    private String hostUrl;
}
