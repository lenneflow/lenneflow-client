package de.lenneflow.lenneflowclient.model;

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
public class UnmanagedClusterDTO {

    private String clusterName;

    private String region;

    private String description;

    private List<String> supportedFunctionTypes = new ArrayList<>();

    private String apiServerEndpoint;

    private String caCertificate;

    private CloudProvider cloudProvider;

    private String cloudCredentialUid;

}
