package de.lenneflow.lenneflowclient.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.lenneflow.lenneflowclient.enums.ControlStructure;
import de.lenneflow.lenneflowclient.enums.RunStatus;
import de.lenneflow.lenneflowclient.model.*;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.text.DateFormatSymbols;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ControllerUtil {

    public ModelMap createModelMap(ModelMap model) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(LocaleContextHolder.getLocale());

        model.addAttribute("loggedinusername", "");
        model.addAttribute("dateFormatSymbols", dateFormatSymbols);
        model.addAttribute("loggedinuser", "");

        return model;
    }

    public String createObjectPayload(Object workflow){
        ObjectMapper om = new ObjectMapper();
        // support Java 8 date time apis
        om.registerModule(new JavaTimeModule());
        ObjectWriter ow = om.writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public  Map<String, Object> convertJsonStringToMap(String jsonString) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        // Convert JSON string to Map
        try {
             map = mapper.readValue(jsonString, Map.class);

        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
        }
        return map;
    }

    public int getNextWorkflowStepOrder(Workflow workflow) {
        List<WorkflowStep> steps = workflow.getSteps();
        if (steps.isEmpty()) {
            return 0;
        }
        List<WorkflowStep> sorted = steps.stream().sorted(Comparator.comparing(WorkflowStep::getExecutionOrder)).toList();
        return sorted.get(sorted.size()-1).getExecutionOrder() + 1;
    }


    public String createWorkflowChartsCode(Workflow workflow){
        StringBuilder sb = new StringBuilder();
        sb.append("st=>start: ").append("Start").append("\n");
        sb.append("e=>end: ").append("End").append("\n");
        List<WorkflowStep> steps = workflow.getSteps();
        List<WorkflowStep> sorted = steps.stream().sorted(Comparator.comparing(WorkflowStep::getExecutionOrder)).toList();
        for (int i = 0; i < sorted.size(); i++) {
                switch (sorted.get(i).getControlStructure()){
                    case SIMPLE:
                        sb.append(sorted.get(i).getName()).append("=>operation: ").append(sorted.get(i).getName()).append("\n");
                        break;
                    case SWITCH:
                        sb.append(sorted.get(i).getName()).append("=>parallel: ").append(sorted.get(i).getName()).append("\n");
                        List<DecisionCase> decisionCases = sorted.get(i).getDecisionCases();
                        for (DecisionCase decisionCase : decisionCases) {
                            sb.append(decisionCase.getName()).append("=>operation: ").append(decisionCase.getName()).append("\n");
                        }
                        break;
                    case DO_WHILE:
                        sb.append(sorted.get(i).getName()).append("=>operation: ").append(sorted.get(i).getName()).append(" (").append(Character.toString(8634)).append(") \n");
                        break;
                    default:
                        break;
                }
        }
        for (int i = 0; i < sorted.size(); i++) {
            if (i == 0) {
                sb.append("st->").append(sorted.get(i).getName()).append("\n");
            }else if(i == sorted.size() - 1) {
                if(sorted.get(i-1).getControlStructure().equals(ControlStructure.SIMPLE)){
                    sb.append(sorted.get(i-1).getName()).append("->").append(sorted.get(i).getName()).append("\n");
                }
                sb.append(sorted.get(i).getName()).append("->e");
            }else{
                switch (sorted.get(i).getControlStructure()){
                    case SIMPLE:
                        sb.append(sorted.get(i-1).getName()).append("->").append(sorted.get(i).getName()).append("\n");
                        break;
                    case SWITCH:
                        sb.append(sorted.get(i-1).getName()).append("->").append(sorted.get(i).getName()).append("\n");
                        String para = sorted.get(i).getName();
                        List<DecisionCase> decisionCases = sorted.get(i).getDecisionCases();
                        for (int j=1; j<=decisionCases.size(); j++) {
                            sb.append(para).append("(").append("path" + j).append(")").append("->").append(decisionCases.get(j-1).getName()).append("->").append(sorted.get(i+1).getName()).append("\n");
                        }
                        break;
                    case DO_WHILE:
                        sb.append(sorted.get(i-1).getName()).append("->").append(sorted.get(i).getName()).append("\n");
                        sb.append(sorted.get(i).getName()).append("->").append(sorted.get(i).getName()).append("\n");
                        break;
                    default:
                        break;
                }
            }


        }
        return sb.toString();
    }


    public String createWorkflowChartsCode(WorkflowExecution execution){
        StringBuilder sb = new StringBuilder();
        sb.append("st=>start: ").append("Start|past").append("\n");
        if(execution.getRunStatus().equals(RunStatus.COMPLETED)){
            sb.append("e=>end: ").append("End|past").append("\n");
        }else{
            sb.append("e=>end: ").append("End|future").append("\n");
        }
        List<WorkflowStepInstance> stepInstances = execution.getRunSteps();
        List<WorkflowStepInstance> sorted = stepInstances.stream().sorted(Comparator.comparing(WorkflowStepInstance::getExecutionOrder)).toList();
        for (int i = 0; i < sorted.size(); i++) {
            switch (sorted.get(i).getControlStructure()){
                case SIMPLE:
                    switch (sorted.get(i).getRunStatus()){
                        case COMPLETED:
                            sb.append(sorted.get(i).getName()).append("=>operation: ").append(sorted.get(i).getName()+"|past").append("\n");
                            break;
                        case RUNNING:
                            sb.append(sorted.get(i).getName()).append("=>operation: ").append(sorted.get(i).getName()+"|current").append("\n");
                            break;
                        default:
                            sb.append(sorted.get(i).getName()).append("=>operation: ").append(sorted.get(i).getName()+"|future").append("\n");
                            break;
                    }
                    break;
                case SWITCH:
                    sb.append(sorted.get(i).getName()).append("=>parallel: ").append(sorted.get(i).getName()).append("\n");
                    List<DecisionCase> decisionCases = sorted.get(i).getDecisionCases();
                    for (DecisionCase decisionCase : decisionCases) {
                        sb.append(decisionCase.getName()).append("=>operation: ").append(decisionCase.getName()).append("\n");
                    }
                    break;
                case DO_WHILE:
                    sb.append(sorted.get(i).getName()).append("=>operation: ").append(sorted.get(i).getName()).append(" (").append(Character.toString(8634)).append(") \n");
                    break;
                default:
                    break;
            }
        }
        for (int i = 0; i < sorted.size(); i++) {
            if (i == 0) {
                sb.append("st->").append(sorted.get(i).getName()).append("\n");
            }else if(i == sorted.size() - 1) {
                if(sorted.get(i-1).getControlStructure().equals(ControlStructure.SIMPLE)){
                    sb.append(sorted.get(i-1).getName()).append("->").append(sorted.get(i).getName()).append("\n");
                }
                sb.append(sorted.get(i).getName()).append("->e");
            }else{
                switch (sorted.get(i).getControlStructure()){
                    case SIMPLE:
                        sb.append(sorted.get(i-1).getName()).append("->").append(sorted.get(i).getName()).append("\n");
                        break;
                    case SWITCH:
                        sb.append(sorted.get(i-1).getName()).append("->").append(sorted.get(i).getName()).append("\n");
                        String para = sorted.get(i).getName();
                        List<DecisionCase> decisionCases = sorted.get(i).getDecisionCases();
                        for (int j=1; j<=decisionCases.size(); j++) {
                            sb.append(para).append("(").append("path" + j).append(")").append("->").append(decisionCases.get(j-1).getName()).append("->").append(sorted.get(i+1).getName()).append("\n");
                        }
                        break;
                    case DO_WHILE:
                        sb.append(sorted.get(i-1).getName()).append("->").append(sorted.get(i).getName()).append("\n");
                        sb.append(sorted.get(i).getName()).append("->").append(sorted.get(i).getName()).append("\n");
                        break;
                    default:
                        break;
                }
            }


        }
        return sb.toString();
    }


}
