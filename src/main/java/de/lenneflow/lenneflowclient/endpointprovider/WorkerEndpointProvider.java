package de.lenneflow.lenneflowclient.endpointprovider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class WorkerEndpointProvider {

    @Value("${lenneflow.root.url}")
    private String rootUrl;

    @Value("${lenneflow.worker.root.url}")
    private String workerRootUrl;

    @Value("${lenneflow.local-cluster.api-server-endpoint}")
    private String localApiServerEndpointPath;

    @Value("${lenneflow.local-cluster.host-url}")
    private String localHostUrlPath;

    @Value("${resource.access-token.create}")
    private String createAccessTokenPath;

    @Value("${resource.cluster.create}")
    private String createCloudClusterPath;

    @Value("${resource.cloud-credential.create}")
    private String createCloudCredentialPath;

    @Value("${resource.cluster.register}")
    private String registerLocalClusterPath;

    @Value("${resource.cluster.find}")
    private String findClusterPath;

    @Value("${resource.cluster.delete}")
    private String deleteClusterPath;

    @Value("${resource.cluster.find-all}")
    private String findAllClustersPath;

    @Value("${resource.access-token.find}")
    private String findAccessTokenPath;

    @Value("${resource.cloud-credential.find}")
    private String findCloudCredentialPath;

    @Value("${resource.cloud-credential.delete}")
    private String deleteCloudCredentialPath;

    @Value("${resource.cloud-credential.find-all}")
    private String findAllCloudCredentialPath;

    @Value("${resource.access-token.delete}")
    private String deleteAccessTokenPath;

    @Value("${resource.access-token.extract}")
    private String extractAccessTokenPath;

    @Value("${resource.access-token.find-all}")
    private String findAllAccessTokenPath;
}
