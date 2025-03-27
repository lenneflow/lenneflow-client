package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.endpointprovider.FunctionEndpointProvider;
import de.lenneflow.lenneflowclient.endpointprovider.OrchestrationEndpointProvider;
import de.lenneflow.lenneflowclient.endpointprovider.WorkerEndpointProvider;
import de.lenneflow.lenneflowclient.endpointprovider.WorkflowEndpointProvider;
import de.lenneflow.lenneflowclient.model.*;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Cluster;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;
    private final FunctionEndpointProvider functionEndpointProvider;
    private final WorkflowEndpointProvider workflowEndpointProvider;
    private final WorkerEndpointProvider workerEndpointProvider;
    private final OrchestrationEndpointProvider orchestrationEndpointProvider;

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
        List<Function> functions = restUtil.getForObjectList(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllFunctionsList(), List.class);
        List<Workflow> workflows = restUtil.getForObjectList(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindAllWorkflowsPath(), List.class);
        List<WorkflowInstance> instances = restUtil.getForObjectList(orchestrationEndpointProvider.getOrchestrationRootUrl() + orchestrationEndpointProvider.getFindAllWorkflowInstancesPath(), List.class);
        List<Cluster> clusters = restUtil.getForObjectList(workerEndpointProvider.getWorkerRootUrl() + workerEndpointProvider.getFindAllClustersPath(), List.class);
        RunStatistic runStatistic = getRunStatistic(instances);
        model.addAttribute("functions", functions);
        model.addAttribute("workflows", workflows);
        model.addAttribute("clusters", clusters);
        model.addAttribute("instances", instances);
        model.addAttribute("runStatistic", runStatistic);
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
        runStatistic.setTotalRuns(instances.size());


        return runStatistic;
    }

}
