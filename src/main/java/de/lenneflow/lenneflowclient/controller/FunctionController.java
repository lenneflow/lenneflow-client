package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.enums.DeploymentState;
import de.lenneflow.lenneflowclient.model.Function;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FunctionController {

    @Value("${function.list.resourcePath}")
    private String functionListResourcePath;

    @Value("${function.root.url}")
    private String functionRootUrl;

    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;

    public FunctionController(RestUtil restUtil, ControllerUtil controllerUtil) {
        this.restUtil = restUtil;
        this.controllerUtil = controllerUtil;
    }


    @GetMapping("/function/list")
    public String functionList(ModelMap model) {
        model = controllerUtil.createModelMap(model);
        List<Function> functions = restUtil.getForObjectList(functionRootUrl + functionListResourcePath, List.class);
        model.addAttribute("functions", functions);
        return "function/function-list";
    }

    @GetMapping("/function/details/{uid}")
    public String functionDetails(ModelMap model, @PathVariable String uid) {
        model = controllerUtil.createModelMap(model);
        Function function = restUtil.getForObject(functionRootUrl + "/" + uid, Function.class);
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
        Function savedFunction = restUtil.postForObject(functionRootUrl, function, Function.class);
        String payload = controllerUtil.createObjectPayload(savedFunction);
        model.addAttribute("function", savedFunction);
        model.addAttribute("payload", payload);
        return "redirect:/function/details/" + savedFunction.getUid();
    }

    @GetMapping("/function/delete/{uid}")
    public String deleteFunction(ModelMap model, @PathVariable String uid) {
        model = controllerUtil.createModelMap(model);
        Function function = restUtil.getForObject(functionRootUrl + "/" + uid, Function.class);
        if(function != null && function.getDeploymentState() == DeploymentState.UNDEPLOYED) {
            restUtil.deleteObject(functionRootUrl + "/" + uid);
        }
        List<Function> functions = restUtil.getForObjectList(functionRootUrl + functionListResourcePath, List.class);
        model.addAttribute("functions", functions);
        return "function/function-list";
    }

    @GetMapping("/function/edit/{uid}")
    public String editFunction(ModelMap model, @PathVariable String uid) {
        model = controllerUtil.createModelMap(model);
        Function function = restUtil.getForObject(functionRootUrl + "/" + uid, Function.class);
        model.addAttribute("function", function);
        model.addAttribute("title", "Edit Function");
        return "function/new-function";
    }
}
