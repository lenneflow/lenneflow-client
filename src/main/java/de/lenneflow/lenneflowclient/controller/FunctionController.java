package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.endpointprovider.FunctionEndpointProvider;
import de.lenneflow.lenneflowclient.enums.DeploymentState;
import de.lenneflow.lenneflowclient.enums.JsonSchemaVersion;
import de.lenneflow.lenneflowclient.model.Function;
import de.lenneflow.lenneflowclient.model.JsonSchema;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FunctionController {


    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;
    private final FunctionEndpointProvider functionEndpointProvider;

    public FunctionController(RestUtil restUtil, ControllerUtil controllerUtil, FunctionEndpointProvider functionEndpointProvider) {
        this.restUtil = restUtil;
        this.controllerUtil = controllerUtil;
        this.functionEndpointProvider = functionEndpointProvider;
    }

    @GetMapping("/function/schema/new")
    public String newFunctionSchemaGet(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        JsonSchema schema = new JsonSchema();
        model.addAttribute("jsonSchema", schema);
        model.addAttribute("schemaVersionList", JsonSchemaVersion.values());
        model.addAttribute("title", "New Function JSON Schema");
        return "schema/new-json-schema";
    }

    @PostMapping("/function/schema/new")
    public String newFunctionSchemaPost(ModelMap model, @ModelAttribute JsonSchema schema) {
        restUtil.postForObject(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getCreateJsonSchemaPath(), schema, JsonSchema.class);
        return "redirect:/function/schema/list";
    }

    @GetMapping("/function/schema/list")
    public String newFunctionSchemaList(ModelMap model, @ModelAttribute JsonSchema schema) {
        model = controllerUtil.createModelMap(model);
        List<JsonSchema> schemaList = restUtil.getForObject(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllJsonSchemaPath(), List.class);
        model.addAttribute("schemaList", schemaList);
        model.addAttribute("title", "Function JSON Schema List");
        return "schema/function-schema-list";
    }

    @GetMapping("/function/list")
    public String functionList(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<Function> functions = restUtil.getForObjectList(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllFunctionsList(), List.class);
        model.addAttribute("functions", functions);
        return "function/function-list";
    }

    @GetMapping("/function/details/{uid}")
    public String functionDetails(ModelMap model, @PathVariable String uid) {
        model = controllerUtil.createModelMap(model);
        Function function = restUtil.getForObject(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindFunctionPath().replace("{uid}", uid), Function.class);
        String payload = controllerUtil.createObjectPayload(function);
        model.addAttribute("function", function);
        model.addAttribute("payload", payload);
        return "function/function-details";
    }

    @GetMapping("/function/new")
    public String newFunctionGet(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        Function function = new Function();
        model.addAttribute("function", function);
        model.addAttribute("title", "New Function");
        return "function/new-function";
    }

    @PostMapping("/function/new")
    public String createFunction(ModelMap model, @ModelAttribute Function function) {
        System.out.println(function.getName());
        model = controllerUtil.createModelMap(model);
        Function savedFunction = restUtil.postForObject(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getCreateFunctionPath(), function, Function.class);
        String payload = controllerUtil.createObjectPayload(savedFunction);
        model.addAttribute("function", savedFunction);
        model.addAttribute("payload", payload);
        return "redirect:/function/details/" + savedFunction.getUid();
    }

    @GetMapping("/function/delete/{uid}")
    public String deleteFunction(ModelMap model, @PathVariable String uid) {
        model = controllerUtil.createModelMap(model);
        Function function = restUtil.getForObject(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getDeleteFunctionPath().replace("{uid}", uid), Function.class);
        if(function != null && function.getDeploymentState() == DeploymentState.UNDEPLOYED) {
            restUtil.deleteObject(functionEndpointProvider.getFunctionRootUrl() + "/" + uid);
        }
        List<Function> functions = restUtil.getForObjectList(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getFindAllFunctionsList(), List.class);
        model.addAttribute("functions", functions);
        return "function/function-list";
    }

    @GetMapping("/function/edit/{uid}")
    public String editFunction(ModelMap model, @PathVariable String uid) {
        model = controllerUtil.createModelMap(model);
        Function function = restUtil.getForObject(functionEndpointProvider.getFunctionRootUrl() + functionEndpointProvider.getCreateFunctionPath(), Function.class);
        model.addAttribute("function", function);
        model.addAttribute("title", "Edit Function");
        return "function/new-function";
    }
}
