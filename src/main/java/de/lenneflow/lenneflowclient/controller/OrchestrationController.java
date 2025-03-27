package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.endpointprovider.OrchestrationEndpointProvider;
import de.lenneflow.lenneflowclient.model.WorkflowExecution;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrchestrationController {

    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;
    private final OrchestrationEndpointProvider orchestrationEndpointProvider;

    public OrchestrationController(RestUtil restUtil, ControllerUtil controllerUtil, OrchestrationEndpointProvider orchestrationEndpointProvider) {
        this.restUtil = restUtil;
        this.controllerUtil = controllerUtil;
        this.orchestrationEndpointProvider = orchestrationEndpointProvider;
    }


    @GetMapping("/executions/list")
    public String workflowRunList(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<WorkflowExecution> executions = restUtil.getForObjectList(orchestrationEndpointProvider.getOrchestrationRootUrl() + orchestrationEndpointProvider.getFindAllWorkflowInstancesPath(), List.class);
        model.addAttribute("executions", executions);
        return "orchestration/run-list";
    }

    @GetMapping("/executions/start/{uid}")
    public String startWorkflowRun(@PathVariable String uid, ModelMap model) {
        model = controllerUtil.createModelMap(model);
        WorkflowExecution execution = restUtil.getForObject(orchestrationEndpointProvider.getOrchestrationRootUrl() + orchestrationEndpointProvider.getStartWorkflowPath().replace("{uid}", uid), WorkflowExecution.class);
        model.addAttribute("execution", execution);
        return "redirect:/executions/" + execution.getRunUid() + "/details";
    }

    @GetMapping("/executions/delete/{uid}")
    public String deleteWorkflowRun(@PathVariable String uid, ModelMap model) {
        restUtil.deleteObject(orchestrationEndpointProvider.getOrchestrationRootUrl() + orchestrationEndpointProvider.getDeleteWorkflowExecutionPath().replace("{uid}", uid));
        return "redirect:/executions/list";
    }

    @GetMapping("/executions/details/{uid}")
    public String getWorkflowRun(@PathVariable String uid, ModelMap model) {
        model = controllerUtil.createModelMap(model);
        WorkflowExecution execution = restUtil.getForObject(orchestrationEndpointProvider.getOrchestrationRootUrl() + orchestrationEndpointProvider.getWorkflowStatePath().replace("{uid}", uid), WorkflowExecution.class);
        String chartsCode = controllerUtil.createFlowChartsCode(execution);
        String payload = controllerUtil.createObjectPayload(execution);
        model.addAttribute("chartsCode", chartsCode);
        model.addAttribute("execution", execution);
        model.addAttribute("payload", payload);
        return "orchestration/run-details";
    }


    private List<WorkflowExecution> addTransients(List<WorkflowExecution> executions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss");
        List<WorkflowExecution> execs = new ArrayList<>();
        for (WorkflowExecution execution : executions) {
            //execution.setStringStartTime(execution.getStartTime().format(formatter));
            //execution.setStringEndTime(execution.getEndTime().format(formatter));
            execs.add(execution);
        }
        return execs;
    }

}
