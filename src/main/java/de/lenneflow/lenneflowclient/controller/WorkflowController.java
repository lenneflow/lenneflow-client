package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.dto.SimpleWorkflowStep;
import de.lenneflow.lenneflowclient.dto.SwitchWorkflowStep;
import de.lenneflow.lenneflowclient.model.DecisionCase;
import de.lenneflow.lenneflowclient.model.Function;
import de.lenneflow.lenneflowclient.model.Workflow;
import de.lenneflow.lenneflowclient.model.WorkflowStep;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WorkflowController {

    @Value("${worflow.get.resourcePath}")
    private String workflowResourcePath;

    @Value("${workflow.root.url}")
    private String workflowRootUrl;

    @Value("${workflowstep.root.url}")
    private String workflowStepRootUrl;

    @Value("${workflow.list.resourcePath}")
    private String workflowListResourcePath;

    @Value("${function.list.resourcePath}")
    private String functionListResourcePath;

    @Value("${function.root.url}")
    private String functionRootUrl;

    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;
    private final ModelMapper modelMapper = new ModelMapper();

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
        Workflow workflow = restUtil.getForObject(workflowRootUrl + "/" +uid, Workflow.class);
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

    @PostMapping("/workflow/new")
    public String newWorkflowPost(ModelMap model, @ModelAttribute Workflow workflow) {
        model = controllerUtil.createModelMap(model);
        Workflow savedWorkflow = restUtil.postForObject(workflowRootUrl, workflow, Workflow.class);
        return "redirect:/workflow/" + savedWorkflow.getUid()+ "/step/new";
    }

    @GetMapping("/workflow/{uid}/step/new")
    public String newWorkflowStepGet(ModelMap model, @PathVariable("uid") String workflowUid) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = restUtil.getForObject(workflowRootUrl+"/"+ workflowUid, Workflow.class);
        WorkflowStep step = new WorkflowStep();
        step.setWorkflowUid(workflowUid);
        step.setWorkflowName(workflow.getName());
        step.setExecutionOrder(controllerUtil.getNextWorkflowStepOrder(workflow));

        List<Function> functions = restUtil.getForObjectList(functionRootUrl + functionListResourcePath, List.class);

        model.addAttribute("functions", functions);
        model.addAttribute("workflow", workflow);
        model.addAttribute("workflowStep", step);
        model.addAttribute("title", "Add Workflow Step for " + workflow.getName());

        return "workflow/new-step";

    }

    @PostMapping("/workflow/{uid}/step/new")
    public String newWorkflowStepPost(ModelMap model, @PathVariable("uid") String workflowUid, @ModelAttribute WorkflowStep workflowStep) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = restUtil.getForObject(workflowRootUrl+workflowResourcePath+"/"+ workflowUid, Workflow.class);
        List<Function> functions = restUtil.getForObjectList(functionRootUrl + functionListResourcePath, List.class);

        model.addAttribute("functions", functions);
        model.addAttribute("workflow", workflow);
        model.addAttribute("workflowStep", workflowStep);
        model.addAttribute("title", "Add Workflow Step for " + workflow.getName());

        switch (workflowStep.getControlStructure()){
            case DO_WHILE:
                return "workflow/new-while-step";
            case SWITCH:
                for(int i=0; i< 2; i++){
                    DecisionCase dc = new DecisionCase();
                    dc.setRetryCount(workflowStep.getRetryCount());
                    workflowStep.getDecisionCases().add(dc);
                }

                return "workflow/new-switch-step";
            case SUB_WORKFLOW:
                return "workflow/new-subworkflow-step";
            default:
                return "workflow/new-simple-step";
        }
    }

    @PostMapping(value = "/workflow/{uid}/step/simple/new")
    public String newSimpleStepPost(ModelMap model,@PathVariable("uid") String workflowUid, @RequestParam String action, @ModelAttribute WorkflowStep workflowStep) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = restUtil.getForObject(workflowRootUrl+workflowResourcePath+"/"+ workflowUid, Workflow.class);
        List<Function> functions = restUtil.getForObjectList(functionRootUrl + functionListResourcePath, List.class);
        workflowStep.setInputData(controllerUtil.convertJsonStringToMap(workflowStep.getStringInputData()));

        model.addAttribute("functions", functions);
        model.addAttribute("workflow", workflow);
        model.addAttribute("title", "Add Workflow Step for " + workflow.getName());
        model.addAttribute("workflowStep", workflowStep);

        switch (action) {
            case "addStep" -> {
                SimpleWorkflowStep simpleStep = modelMapper.map(workflowStep, SimpleWorkflowStep.class);
                restUtil.postForObject(workflowStepRootUrl + "/simple", simpleStep, WorkflowStep.class);
                WorkflowStep step = new WorkflowStep();
                step.setExecutionOrder(controllerUtil.getNextWorkflowStepOrder(workflow));
                model.addAttribute("workflowStep", step);
                return "workflow/new-step";
            }
            case "saveStep" -> {
                SimpleWorkflowStep simpleStep = modelMapper.map(workflowStep, SimpleWorkflowStep.class);
                restUtil.postForObject(workflowStepRootUrl + "/simple", simpleStep, WorkflowStep.class);
                return "redirect:/workflow/details/" + workflowUid;
            }
        }

        return "redirect:/workflow/details/" + workflowUid;
    }

    @PostMapping(value = "/workflow/{uid}/step/switch/new")
    public String newSwitchStepPost(ModelMap model,@PathVariable("uid") String workflowUid, @RequestParam String action, @ModelAttribute WorkflowStep workflowStep) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = restUtil.getForObject(workflowRootUrl+workflowResourcePath+"/"+ workflowUid, Workflow.class);
        List<Function> functions = restUtil.getForObjectList(functionRootUrl + functionListResourcePath, List.class);
        workflowStep.setInputData(controllerUtil.convertJsonStringToMap(workflowStep.getStringInputData()));

        model.addAttribute("functions", functions);
        model.addAttribute("workflow", workflow);
        model.addAttribute("title", "Add Workflow Step for " + workflow.getName());
        model.addAttribute("workflowStep", workflowStep);

        switch (action) {
            case "addDecisionCase" -> {
                DecisionCase dc = new DecisionCase();
                dc.setRetryCount(workflowStep.getRetryCount());
                workflowStep.getDecisionCases().add(dc);
                return "workflow/new-switch-step";
            }
            case "removeDecisionCase" -> {
                if (workflowStep.getDecisionCases().size() > 2) {
                    workflowStep.getDecisionCases().remove(workflowStep.getDecisionCases().get(workflowStep.getDecisionCases().size() - 1));
                }
                return "workflow/new-switch-step";
            }
            case "addStep" -> {
                SwitchWorkflowStep switchStep = modelMapper.map(workflowStep, SwitchWorkflowStep.class);
                restUtil.postForObject(workflowStepRootUrl + "/switch", switchStep, WorkflowStep.class);
                WorkflowStep step = new WorkflowStep();
                step.setExecutionOrder(controllerUtil.getNextWorkflowStepOrder(workflow));
                model.addAttribute("workflowStep", step);
                return "workflow/new-step";
            }
            case "saveStep" -> {
                SwitchWorkflowStep switchStep = modelMapper.map(workflowStep, SwitchWorkflowStep.class);
                restUtil.postForObject(workflowStepRootUrl + "/switch", switchStep, WorkflowStep.class);
                return "redirect:/workflow/details/" + workflowUid;
            }
        }
        return "redirect:/workflow/details/" + workflowUid ;
    }


}
