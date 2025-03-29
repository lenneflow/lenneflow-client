package de.lenneflow.lenneflowclient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.lenneflow.lenneflowclient.endpointprovider.FunctionEndpointProvider;
import de.lenneflow.lenneflowclient.endpointprovider.OrchestrationEndpointProvider;
import de.lenneflow.lenneflowclient.endpointprovider.WorkerEndpointProvider;
import de.lenneflow.lenneflowclient.endpointprovider.WorkflowEndpointProvider;
import de.lenneflow.lenneflowclient.enums.RunStatus;
import de.lenneflow.lenneflowclient.model.*;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;
    private final FunctionEndpointProvider functionEndpointProvider;
    private final WorkflowEndpointProvider workflowEndpointProvider;
    private final WorkerEndpointProvider workerEndpointProvider;
    private final OrchestrationEndpointProvider orchestrationEndpointProvider;
    public final ObjectMapper objectMapper = new ObjectMapper();

    public HomeController(RestUtil restUtil, ControllerUtil controllerUtil, FunctionEndpointProvider functionEndpointProvider, WorkflowEndpointProvider workflowEndpointProvider, WorkerEndpointProvider workerEndpointProvider, OrchestrationEndpointProvider orchestrationEndpointProvider) {
        this.restUtil = restUtil;
        this.controllerUtil = controllerUtil;
        this.functionEndpointProvider = functionEndpointProvider;
        this.workflowEndpointProvider = workflowEndpointProvider;
        this.workerEndpointProvider = workerEndpointProvider;
        this.orchestrationEndpointProvider = orchestrationEndpointProvider;
    }

    @GetMapping(value = {"", "/"})
    public String home(ModelMap model){
        model = controllerUtil.createModelMap(model);
        List<Function> functions = restUtil.getForObjectList(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllFunctionsList(), new ParameterizedTypeReference<List<Function>>() {});
        List<Workflow> workflows = restUtil.getForObjectList(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindAllWorkflowsPath(), new ParameterizedTypeReference<List<Workflow>>() {});
        List<WorkflowInstance> instances = restUtil.getForObjectList(orchestrationEndpointProvider.getOrchestrationRootUrl() + orchestrationEndpointProvider.getFindAllWorkflowInstancesPath(),  new ParameterizedTypeReference<List<WorkflowInstance>>() {});
        List<WorkflowInstance> runningInstances = instances.stream().filter(instance -> instance.getRunStatus().equals(RunStatus.RUNNING)).collect(Collectors.toList());
        List<WorkflowInstance> pausedInstances = instances.stream().filter(instance -> instance.getRunStatus().equals(RunStatus.PAUSED)).collect(Collectors.toList());
        List<KubernetesCluster> clusters = restUtil.getForObjectList(workerEndpointProvider.getWorkerRootUrl() + workerEndpointProvider.getFindAllClustersPath(), new ParameterizedTypeReference<List<KubernetesCluster>>() {});
        RunStatistic runStatistic = getRunStatistic(instances);
        model.addAttribute("functions", functions);
        model.addAttribute("workflows", workflows);
        model.addAttribute("clusters", clusters);
        model.addAttribute("instances", instances);
        model.addAttribute("runningInstances", runningInstances);
        model.addAttribute("pausedInstances", pausedInstances);
        model.addAttribute("runStatistic", runStatistic);
//        model.addAttribute("successRate", runStatistic.getSuccessRate());
//        model.addAttribute("failureRate", runStatistic.getFailureRate());
//        model.addAttribute("runningRate", runStatistic.getRunningRate());
        return "index";
    }

    @GetMapping("/login")
    public String login(ModelMap model) {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/403";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login?logout";
    }

    private RunStatistic getRunStatistic(List<WorkflowInstance> instances){
        RunStatistic runStatistic = new RunStatistic();
        int total = instances.size();
        int running = 0;
        int completed =  0;
        int failed = 0;

        for (WorkflowInstance instance : instances){
            if (instance.getRunStatus().equals(RunStatus.RUNNING)){
                running++;
            } else if (instance.getRunStatus().equals(RunStatus.COMPLETED)){
                completed++;
            } else if (instance.getRunStatus().equals(RunStatus.FAILED)){
                failed++;
            }
        }
        runStatistic.setFailureRate((failed / total) * 100L);
        runStatistic.setSuccessRate((completed / total) * 100L);
        runStatistic.setRunningRate((running / total) * 100L);

        return runStatistic;

    }

}
