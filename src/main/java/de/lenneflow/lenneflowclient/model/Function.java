package de.lenneflow.lenneflowclient.model;


import de.lenneflow.lenneflowclient.enums.DeploymentState;
import de.lenneflow.lenneflowclient.enums.PackageRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Function {

    private String uid;

    private String name;

    private String description;

    private String type;

    private PackageRepository packageRepository;

    private DeploymentState deploymentState = DeploymentState.UNDEPLOYED;

    private String resourcePath;

    private String imageName;

    private boolean lazyDeployment;

    private int servicePort;

    private int assignedHostPort;

    private String serviceUrl;

    private LocalDateTime creationTime;

    private LocalDateTime updateTime;

    private String inputSchema;

}

