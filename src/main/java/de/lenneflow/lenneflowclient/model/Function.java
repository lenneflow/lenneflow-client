package de.lenneflow.lenneflowclient.model;


import de.lenneflow.lenneflowclient.enums.DeploymentState;
import de.lenneflow.lenneflowclient.enums.PackageRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Function {

    private String uid;

    private String name;

    private String description;

    private String type;

    private DeploymentState deploymentState;

    private PackageRepository packageRepository;

    private String resourcePath;

    private int servicePort;

    private boolean lazyDeployment;

    private String imageName;

    private String cpuRequest;

    private String memoryRequest;

    private String inputSchemaUid;

    private String outputSchemaUid;

}

