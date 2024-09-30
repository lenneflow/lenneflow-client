package de.lenneflow.lenneflowclient.controller;

import de.lenneflow.lenneflowclient.model.Function;
import de.lenneflow.lenneflowclient.util.ControllerUtil;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Value("${function.list.resourcePath}")
    private String functionListResourcePath;

    @Value("${function.root.url}")
    private String functionRootUrl;

    private final RestUtil restUtil;
    private final ControllerUtil controllerUtil;

    public HomeController(RestUtil restUtil, ControllerUtil controllerUtil) {
        this.restUtil = restUtil;
        this.controllerUtil = controllerUtil;
    }

    @GetMapping(value = {"", "/"})
    public String home(ModelMap model){
        model = controllerUtil.createModelMap(model);
        List<Function> functions = restUtil.getForObjectList(functionRootUrl + functionListResourcePath, List.class);
        model.addAttribute("functions", functions);
        return "index";
    }
}
