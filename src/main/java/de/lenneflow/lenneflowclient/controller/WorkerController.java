package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.endpointprovider.FunctionEndpointProvider;
import de.lenneflow.lenneflowclient.endpointprovider.WorkerEndpointProvider;
import de.lenneflow.lenneflowclient.endpointprovider.WorkflowEndpointProvider;
import de.lenneflow.lenneflowclient.enums.JsonSchemaVersion;
import de.lenneflow.lenneflowclient.model.*;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class WorkerController {

    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;
    private final FunctionEndpointProvider functionEndpointProvider;
    private final WorkflowEndpointProvider workflowEndpointProvider;
    private final WorkerEndpointProvider workerEndpointProvider;

    public WorkerController(RestUtil restUtil, ControllerUtil controllerUtil, FunctionEndpointProvider functionEndpointProvider, WorkflowEndpointProvider workflowEndpointProvider, WorkerEndpointProvider workerEndpointProvider) {
        this.restUtil = restUtil;
        this.controllerUtil = controllerUtil;
        this.functionEndpointProvider = functionEndpointProvider;
        this.workflowEndpointProvider = workflowEndpointProvider;
        this.workerEndpointProvider = workerEndpointProvider;
    }
    @GetMapping("/worker/k8s/token/list")
    public String KubernetesTokenList(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<AccessTokenDto> accessTokens = restUtil.getForObjectList(workerEndpointProvider.getWorkerRootUrl() + workerEndpointProvider.getFindAllAccessTokenPath(), List.class);
        model.addAttribute("accessTokens", accessTokens);
        model.addAttribute("title", "Kubernetes Access Token List");
        return "worker/access-token-list";
    }

    @GetMapping("/worker/cloud/credential/new")
    public String newCloudCredentialGet(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        CloudCredentialDTO cloudCredential = new CloudCredentialDTO();
        model.addAttribute("cloudCredential", cloudCredential);
        model.addAttribute("title", "New Cloud Credential");
        return "worker/new-cloud-credential";
    }

    @PostMapping("/worker/cloud/credential/new")
    public String newCloudCredentialPost(ModelMap model, CloudCredentialDTO cloudCredential) {
        restUtil.postForObject(workerEndpointProvider.getWorkerRootUrl() + workerEndpointProvider.getCreateCloudCredentialPath(), cloudCredential, CloudCredentialDTO.class);
        return "redirect:/worker/cloud/credential/list";
    }

    @GetMapping("/worker/cloud/credential/list")
    public String cloudCredentialList(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<CloudCredentialDTO> cloudCredentials = restUtil.getForObjectList(workerEndpointProvider.getWorkerRootUrl() + workerEndpointProvider.getFindAllCloudCredentialPath(), List.class);
        model.addAttribute("cloudCredentials", cloudCredentials);
        model.addAttribute("title", "Cloud Credential List");
        return "worker/cloud-credential-list";
    }

    @GetMapping("/worker/cloud/cluster/new")
    public String newCloudClusterGet(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        ManagedClusterDTO managedCluster = new ManagedClusterDTO();
        model.addAttribute("cluster", managedCluster);
        model.addAttribute("title", "New Managed Cluster");
        return "worker/new-cluster";
    }

    @PostMapping("/worker/cloud/cluster/new")
    public String newCloudClusterPost(ModelMap model, ManagedClusterDTO managedClusterDTO) {
        KubernetesCluster saved = restUtil.postForObject(workerEndpointProvider.getWorkerRootUrl() + workerEndpointProvider.getCreateCloudClusterPath(), managedClusterDTO, KubernetesCluster.class);
        return "redirect:/worker/cloud/cluster/{uid}/details".replace("{uid}", saved.getUid());
    }

    @GetMapping("/worker/cloud/cluster/{uid}/details")
    public String cloudClusterDetails(ModelMap model, @PathVariable String uid) {
        model = controllerUtil.createModelMap(model);
        KubernetesCluster cluster = restUtil.getForObject(workerEndpointProvider.getWorkerRootUrl() + workerEndpointProvider.getFindClusterPath(), KubernetesCluster.class);
        List<CloudCredentialDTO> cloudCredentials = restUtil.getForObjectList(workerEndpointProvider.getWorkerRootUrl() + workerEndpointProvider.getFindAllCloudCredentialPath(), List.class);
        model.addAttribute("cluster", cluster);
        model.addAttribute("credentials", cloudCredentials);
        model.addAttribute("title", "Cluster Information");
        return "worker/cloud-cluster-details";
    }

    @GetMapping("/worker/cluster/list")
    public String cloudClusterList(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<KubernetesCluster> clusters = restUtil.getForObjectList(workerEndpointProvider.getWorkerRootUrl() + workerEndpointProvider.getFindAllClustersPath(), List.class);
        model.addAttribute("clusters", clusters);
        model.addAttribute("title", "Cloud Cluster List");
        return "worker/cluster-list";
    }

}
