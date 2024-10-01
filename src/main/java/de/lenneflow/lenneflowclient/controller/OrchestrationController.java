package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.model.Workflow;
import de.lenneflow.lenneflowclient.model.WorkflowExecution;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrchestrationController {

    @Value("${orchestration.runs.resourcePath}")
    private String executionsListResourcePath;

    @Value("${orchestration.root.url}")
    private String orchestrationRootUrl;

    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;

    public OrchestrationController(RestUtil restUtil, ControllerUtil controllerUtil) {
        this.restUtil = restUtil;
        this.controllerUtil = controllerUtil;
    }


    @GetMapping("/executions/list")
    public String workflowRunList(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<WorkflowExecution> executions = restUtil.getForObjectList(orchestrationRootUrl + executionsListResourcePath, List.class);
        model.addAttribute("executions", executions);
        return "orchestration/run-list";
    }

    @GetMapping("/executions/start/{uid}")
    public String startWorkflowRun(@PathVariable String uid, ModelMap model) {
        model = controllerUtil.createModelMap(model);
        WorkflowExecution execution = restUtil.getForObject(orchestrationRootUrl + "/workflows/" + uid + "/start", WorkflowExecution.class);
        model.addAttribute("execution", execution);
        return "redirect:/executions/" + execution.getRunId() + "/details";
    }

    @GetMapping("/executions/{uid}/details")
    public String getWorkflowRun(@PathVariable String uid, ModelMap model) {
        model = controllerUtil.createModelMap(model);
        WorkflowExecution execution = restUtil.getForObject(orchestrationRootUrl + executionsListResourcePath + "/" +uid + "/state", WorkflowExecution.class);
        String chartsCode = controllerUtil.createWorkflowChartsCode(execution);
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
            execution.setStringStartTime(execution.getStartTime().format(formatter));
            execution.setStringEndTime(execution.getEndTime().format(formatter));
            execs.add(execution);
        }
        return execs;
    }

}
