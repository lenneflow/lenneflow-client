package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.model.Function;
import de.lenneflow.lenneflowclient.model.Workflow;
import de.lenneflow.lenneflowclient.model.WorkflowStep;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class WorkflowController {

    @Value("${worflow.get.resourcePath}")
    private String workflowResourcePath;

    @Value("${workflow.root.url}")
    private String workflowRootUrl;

    @Value("${workflow.list.resourcePath}")
    private String workflowListResourcePath;

    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;

    public WorkflowController(RestUtil restUtil, ControllerUtil controllerUtil) {
        this.restUtil = restUtil;
        this.controllerUtil = controllerUtil;
    }

    @GetMapping("/workflow/list")
    public String workflowListList(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<Workflow> workflows = restUtil.getForObjectList(workflowRootUrl + workflowListResourcePath, List.class);
        model.addAttribute("workflows", workflows);
        return "workflow/workflow-list";
    }


    @GetMapping("/workflow/details/{uid}")
    public String getWorkflow(@PathVariable String uid,  ModelMap model) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = restUtil.getForObject(workflowRootUrl+workflowResourcePath+"/"+uid, Workflow.class);
        String chartsCode = controllerUtil.createWorkflowChartsCode(workflow);
        String payload = controllerUtil.createObjectPayload(workflow);
        model.addAttribute("chartsCode", chartsCode);
        model.addAttribute("workflow", workflow);
        model.addAttribute("payload", payload);
        return "workflow/workflow-view";
    }

    @GetMapping("/workflow/new")
    public String newWorkflowGet(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = new Workflow();
        model.addAttribute("workflow", workflow);
        model.addAttribute("title", "New Workflow");
        return "workflow/new-workflow";
    }

    @GetMapping("/workflow/{uid}/step/new")
    public String newWorkflowStepGet(ModelMap model, @PathVariable("uid") String workflowUid) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = restUtil.getForObject(workflowRootUrl+workflowResourcePath+"/"+ workflowUid, Workflow.class);
        WorkflowStep step = new WorkflowStep();
        step.setWorkflowUid(workflowUid);
        model.addAttribute("workflow", workflow);
        model.addAttribute("workflowStep", step);
        model.addAttribute("title", "Add Workflow Step for " + workflow.getName());
        return "workflow/new-workflow-step";
    }


}
