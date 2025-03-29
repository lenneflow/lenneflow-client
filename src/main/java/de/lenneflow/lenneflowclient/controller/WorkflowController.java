package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.dto.WorkflowDTO;
import de.lenneflow.lenneflowclient.endpointprovider.FunctionEndpointProvider;
import de.lenneflow.lenneflowclient.endpointprovider.WorkflowEndpointProvider;
import de.lenneflow.lenneflowclient.enums.ControlStructure;
import de.lenneflow.lenneflowclient.enums.JsonSchemaVersion;
import de.lenneflow.lenneflowclient.model.*;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkflowController {



    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;
    private final ModelMapper modelMapper = new ModelMapper();
    private final FunctionEndpointProvider functionEndpointProvider;
    private  final WorkflowEndpointProvider workflowEndpointProvider;

    public WorkflowController(RestUtil restUtil, ControllerUtil controllerUtil, FunctionEndpointProvider functionEndpointProvider, WorkflowEndpointProvider workflowEndpointProvider) {
        this.restUtil = restUtil;
        this.controllerUtil = controllerUtil;
        this.functionEndpointProvider = functionEndpointProvider;
        this.workflowEndpointProvider = workflowEndpointProvider;
    }

    @GetMapping("/workflow/list")
    public String workflowListList(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<Workflow> workflows = restUtil.getForObjectList(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindAllWorkflowsPath(), new ParameterizedTypeReference<List<Workflow>>(){});
        model.addAttribute("workflows", workflows);
        return "workflow/workflow-list";
    }


    @GetMapping("/workflow/new")
    public String newWorkflowGet(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        WorkflowDTO workflow = new WorkflowDTO();
        List<JsonSchema> schemaList = restUtil.getForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindAllJsonSchemaPath(), List.class);
        model.addAttribute("workflow", workflow);
        model.addAttribute("schemaList", schemaList);
        model.addAttribute("title", "New Workflow");
        return "workflow/new-workflow";
    }

    @GetMapping("/workflow/details/{uid}")
    public String getWorkflow(@PathVariable String uid,  ModelMap model) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = restUtil.getForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindWorkflowPath().replace("{uid}", uid), Workflow.class);
        String chartsCode = controllerUtil.createFlowChartsCode(workflow);
        String payload = controllerUtil.createObjectPayload(workflow);
        model.addAttribute("chartsCode", chartsCode);
        model.addAttribute("workflow", workflow);
        model.addAttribute("payload", payload);
        return "workflow/workflow-view";
    }

    @GetMapping("/workflow/schema/new")
    public String newWorkflowSchemaGet(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        JsonSchema schema = new JsonSchema();
        model.addAttribute("jsonSchema", schema);
        model.addAttribute("schemaVersionList", JsonSchemaVersion.values());
        model.addAttribute("title", "New Workflow JSON Schema");
        return "schema/new-json-schema";
    }

    @GetMapping("/workflow/schema/edit/{uid}")
    public String editWorkflowSchemaGet(ModelMap model, @PathVariable String uid) {
        model = controllerUtil.createModelMap(model);
        JsonSchema schema = restUtil.getForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindJsonSchemaPath().replace("{uid}", uid), JsonSchema.class);
        model.addAttribute("jsonSchema", schema);
        model.addAttribute("schemaVersionList", JsonSchemaVersion.values());
        model.addAttribute("title", "Edit JSON Schema");
        return "schema/new-json-schema";
    }

    @PostMapping("/workflow/schema/new")
    public String newWorkflowSchemaPost(ModelMap model, @ModelAttribute JsonSchema schema) {
        restUtil.postForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getCreateJsonSchemaPath(), schema, JsonSchema.class);
        return "redirect:/workflow/schema/list";
    }

    @GetMapping("/workflow/schema/delete/{uid}")
    public String deleteWorkflowSchemaGet(ModelMap model, @PathVariable String uid) {
        restUtil.deleteObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getDeleteJsonSchemaPath().replace("{uid}", uid));
        return "redirect:/workflow/schema/list";
    }

    @GetMapping("/workflow/schema/list")
    public String newWorkflowSchemaList(ModelMap model, @ModelAttribute JsonSchema schema) {
        model = controllerUtil.createModelMap(model);
        List<JsonSchema> schemaList = restUtil.getForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindAllJsonSchemaPath(), List.class);
        model.addAttribute("schemaList", schemaList);
        model.addAttribute("title", "Workflow JSON Schema List");
        return "schema/workflow-schema-list";
    }


    @PostMapping("/workflow/new")
    public String newWorkflowPost(ModelMap model, @ModelAttribute WorkflowDTO workflow) {
        Workflow savedWorkflow = restUtil.postForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getCreateWorkflowPath(), workflow, Workflow.class);
        return "redirect:/workflow/details/" + savedWorkflow.getUid();
    }

    @GetMapping("/workflow/{uid}/step/new")
    public String newWorkflowStepGet(ModelMap model, @PathVariable("uid") String workflowUid) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = restUtil.getForObject(workflowEndpointProvider.getWorkflowRootUrl()+ workflowEndpointProvider.getFindWorkflowPath().replace("{uid}", workflowUid), Workflow.class);
        WorkflowStep step = new WorkflowStep();
        step.setWorkflowUid(workflowUid);
        step.setWorkflowName(workflow.getName());
        step.setExecutionOrder(controllerUtil.getNextWorkflowStepOrder(workflow));

        List<Function> functions = restUtil.getForObjectList(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllFunctionsList(), new ParameterizedTypeReference<List<Function>>(){});

        model.addAttribute("functions", functions);
        model.addAttribute("workflow", workflow);
        model.addAttribute("workflowStep", step);
        model.addAttribute("ctlStructures", ControlStructure.values());
        model.addAttribute("title", "Add Workflow Step for " + workflow.getName());

        return "workflow/new-step";

    }

    @PostMapping("/workflow/{uid}/step/new")
    public String newWorkflowStepPost(ModelMap model, @PathVariable("uid") String workflowUid, @ModelAttribute WorkflowStep workflowStep) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = restUtil.getForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindWorkflowPath().replace("{uid}", workflowUid), Workflow.class);
        List<Function> functions = restUtil.getForObjectList(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllFunctionsList(), new ParameterizedTypeReference<List<Function>>(){});
        List<Workflow> workflows = restUtil.getForObjectList(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindAllWorkflowsPath(), new ParameterizedTypeReference<List<Workflow>>(){});

        model.addAttribute("functions", functions);
        model.addAttribute("workflow", workflow);
        model.addAttribute("workflows", workflows);
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
        SimpleWorkflowStep simpleStep = new SimpleWorkflowStep();
        Workflow workflow = restUtil.getForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindWorkflowPath().replace("{uid}", workflowUid), Workflow.class);
        List<Function> functions = restUtil.getForObjectList(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllFunctionsList(), new ParameterizedTypeReference<List<Function>>(){});
        simpleStep.setName(workflowStep.getName());
        simpleStep.setWorkflowUid(workflowUid);
        simpleStep.setFunctionUid(workflowStep.getFunctionUid());
        simpleStep.setExecutionOrder(workflowStep.getExecutionOrder());
        simpleStep.setRetryCount(workflowStep.getRetryCount());
        simpleStep.setDescription(workflowStep.getDescription());
        simpleStep.setInputData(controllerUtil.convertJsonStringToMap(workflowStep.getStringInputData()));

        model.addAttribute("functions", functions);
        model.addAttribute("workflow", workflow);
        model.addAttribute("title", "Add Workflow Step for " + workflow.getName());
        model.addAttribute("workflowStep", workflowStep);

        restUtil.postForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getCreateSimpleWorkflowStepPath(), simpleStep, WorkflowStep.class);

        if(action.equals("addStep")){
            WorkflowStep step = new WorkflowStep();
            step.setExecutionOrder(controllerUtil.getNextWorkflowStepOrder(workflow));
            model.addAttribute("workflowStep", step);
            return "workflow/new-step";
        }

        return "redirect:/workflow/details/" + workflowUid;
    }

    @PostMapping(value = "/workflow/{uid}/step/switch/new")
    public String newSwitchStepPost(ModelMap model,@PathVariable("uid") String workflowUid, @RequestParam String action, @ModelAttribute WorkflowStep workflowStep) {
        model = controllerUtil.createModelMap(model);
        Workflow workflow = restUtil.getForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindWorkflowPath().replace("{uid}", workflowUid), Workflow.class);
        List<Function> functions = restUtil.getForObjectList(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllFunctionsList(), new ParameterizedTypeReference<List<Function>>(){});
        SwitchWorkflowStep switchStep = new SwitchWorkflowStep();
        switchStep.setName(workflowStep.getName());
        switchStep.setWorkflowUid(workflowUid);
        switchStep.setSwitchCase(workflowStep.getSwitchCase());
        switchStep.setRetryCount(workflowStep.getRetryCount());
        switchStep.setExecutionOrder(workflowStep.getExecutionOrder());
        switchStep.setDescription(workflowStep.getDescription());

        List<DecisionCase> decisionCases = new ArrayList<>();
        for (DecisionCase dc : workflowStep.getDecisionCases()) {
            dc.setRetryCount(workflowStep.getRetryCount());
            dc.setInputData(controllerUtil.convertJsonStringToMap(dc.getStringInputData()));
            decisionCases.add(dc);
        }
        switchStep.setDecisionCases(decisionCases);

        model.addAttribute("functions", functions);
        model.addAttribute("workflow", workflow);
        model.addAttribute("title", "Add Workflow Step for " + workflow.getName());
        model.addAttribute("workflowStep", workflowStep);

        switch (action) {
            case "addDecisionCase" -> {
                DecisionCase dc = new DecisionCase();
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
                restUtil.postForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getCreateSwitchWorkflowStepPath(), switchStep, WorkflowStep.class);
                WorkflowStep step = new WorkflowStep();
                step.setExecutionOrder(controllerUtil.getNextWorkflowStepOrder(workflow));
                model.addAttribute("workflowStep", step);
                return "workflow/new-step";
            }
            default -> {
                restUtil.postForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getCreateSwitchWorkflowStepPath(), switchStep, WorkflowStep.class);
                return "redirect:/workflow/details/" + workflowUid;
            }
        }
    }


    @PostMapping(value = "/workflow/{uid}/step/while/new")
    public String newWhileStepPost(ModelMap model,@PathVariable("uid") String workflowUid, @RequestParam String action, @ModelAttribute WorkflowStep workflowStep) {
        model = controllerUtil.createModelMap(model);
        WhileWorkflowStep whileStep = new WhileWorkflowStep();
        Workflow workflow = restUtil.getForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindWorkflowPath().replace("{uid}", workflowUid), Workflow.class);
        List<Function> functions = restUtil.getForObjectList(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllFunctionsList(), new ParameterizedTypeReference<List<Function>>(){});
        whileStep.setName(workflowStep.getName());
        whileStep.setWorkflowUid(workflowUid);
        whileStep.setFunctionUid(workflowStep.getFunctionUid());
        whileStep.setExecutionOrder(workflowStep.getExecutionOrder());
        whileStep.setRetryCount(workflowStep.getRetryCount());
        whileStep.setDescription(workflowStep.getDescription());
        whileStep.setStopCondition(workflowStep.getStopCondition());
        whileStep.setInputData(controllerUtil.convertJsonStringToMap(workflowStep.getStringInputData()));

        model.addAttribute("functions", functions);
        model.addAttribute("workflow", workflow);
        model.addAttribute("title", "Add Workflow Step for " + workflow.getName());
        model.addAttribute("workflowStep", workflowStep);

        restUtil.postForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getCreateWhileWorkflowStepPath(), whileStep, WorkflowStep.class);

        if(action.equals("addStep")){
            WorkflowStep step = new WorkflowStep();
            step.setExecutionOrder(controllerUtil.getNextWorkflowStepOrder(workflow));
            model.addAttribute("workflowStep", step);
            return "workflow/new-step";
        }
        return "redirect:/workflow/details/" + workflowUid;
    }

    @PostMapping(value = "/workflow/{uid}/step/subworkflow/new")
    public String newSubWorkflowStepPost(ModelMap model,@PathVariable("uid") String workflowUid, @RequestParam String action, @ModelAttribute WorkflowStep workflowStep) {
        model = controllerUtil.createModelMap(model);
        SubWorkflowStep subWorkflowStep = new SubWorkflowStep();
        Workflow workflow = restUtil.getForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getFindWorkflowPath().replace("{uid}", workflowUid), Workflow.class);
        List<Function> functions = restUtil.getForObjectList(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllFunctionsList(),new ParameterizedTypeReference<List<Function>>(){});
        subWorkflowStep.setName(workflowStep.getName());
        subWorkflowStep.setWorkflowUid(workflowUid);
        subWorkflowStep.setSubWorkflowUid(workflowStep.getSubWorkflowUid());
        subWorkflowStep.setExecutionOrder(workflowStep.getExecutionOrder());
        subWorkflowStep.setRetryCount(workflowStep.getRetryCount());
        subWorkflowStep.setDescription(workflowStep.getDescription());
        if(workflowStep.getInputData() != null && !workflowStep.getInputData().isEmpty()){
            subWorkflowStep.setInputData(controllerUtil.convertJsonStringToMap(workflowStep.getStringInputData()));
        }

        model.addAttribute("functions", functions);
        model.addAttribute("workflow", workflow);
        model.addAttribute("title", "Add Workflow Step for " + workflow.getName());
        model.addAttribute("workflowStep", workflowStep);

        restUtil.postForObject(workflowEndpointProvider.getWorkflowRootUrl() + workflowEndpointProvider.getCreateSubWorkflowStepPath(), subWorkflowStep, WorkflowStep.class);

        if(action.equals("addStep")){
            WorkflowStep step = new WorkflowStep();
            step.setExecutionOrder(controllerUtil.getNextWorkflowStepOrder(workflow));
            model.addAttribute("workflowStep", step);
            return "workflow/new-step";
        }
        return "redirect:/workflow/details/" + workflowUid;
    }
}
