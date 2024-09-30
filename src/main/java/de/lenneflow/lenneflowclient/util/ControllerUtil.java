package de.lenneflow.lenneflowclient.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.lenneflow.lenneflowclient.model.Workflow;
import de.lenneflow.lenneflowclient.model.WorkflowStep;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.text.DateFormatSymbols;
import java.util.Comparator;
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

    public String createWorkflowChartsCode(Workflow workflow){
        StringBuilder sb = new StringBuilder();
        List<WorkflowStep> steps = workflow.getSteps();
        List<WorkflowStep> sorted = steps.stream().sorted(Comparator.comparing(WorkflowStep::getExecutionOrder)).toList();
        for (int i = 0; i < sorted.size(); i++) {
            if (i == 0) {
                sb.append("st=>start: ").append(sorted.get(i).getName()).append("\n");
            }else if (i == sorted.size() - 1) {
                sb.append("e=>end: ").append(sorted.get(i).getName()).append("\n");
            }else{
                switch (sorted.get(i).getControlStructure()){
                    case SIMPLE:
                        sb.append(sorted.get(i).getName()).append("=>operation: ").append(sorted.get(i).getName()).append("\n");
                        break;
                    case SWITCH:
                        sb.append(sorted.get(i).getName()).append("=>parallel: ").append(sorted.get(i).getName()).append("\n");
                        Map<String, WorkflowStep> decisionCases = sorted.get(i).getDecisionCases();
                        for (Map.Entry<String, WorkflowStep> entry : decisionCases.entrySet()) {
                            sb.append(entry.getKey()).append("=>operation: ").append(entry.getValue().getName()).append("\n");
                        }
                        break;
                    case DO_WHILE:
                        sb.append(sorted.get(i).getName()).append("=>operation: ").append(sorted.get(i).getName()).append(" (").append(Character.toString(8634)).append(") \n");
                        break;
                    default:
                        break;
                }
            }

        }
        for (int i = 0; i < sorted.size(); i++) {
            if (i == 0) {
                sb.append("st->").append(sorted.get(i+1).getName()).append("\n");
            }else if(i == sorted.size() - 2) {
                sb.append(sorted.get(i).getName()).append("->e");
            }else if(i != sorted.size() - 1){
                switch (sorted.get(i).getControlStructure()){
                    case SIMPLE:
                        sb.append(sorted.get(i).getName()).append("->").append(sorted.get(i+1).getName()).append("\n");
                        break;
                    case SWITCH:
                        String para = sorted.get(i).getName();
                        Map<String, WorkflowStep> decisionCases = sorted.get(i).getDecisionCases();
                        for (Map.Entry<String, WorkflowStep> entry : decisionCases.entrySet()) {
                            sb.append(para).append("(").append(entry.getKey()).append(")").append("->").append(entry.getValue().getName()).append("->").append(sorted.get(i).getName()).append("\n");
                        }
                        break;
                    case DO_WHILE:
                        sb.append(sorted.get(i).getName()).append("->").append(sorted.get(i+1).getName()).append("\n");
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
