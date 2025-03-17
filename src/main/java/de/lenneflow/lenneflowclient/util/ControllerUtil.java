package de.lenneflow.lenneflowclient.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    private static final String[] positions = {"left", "bottom", "right", "top"};
    private static final String NODE_TYPE_OPERATION = "=>operation: ";
    private static final String NODE_TYPE_PARALLEL = "=>parallel: ";
    private static final String NODE_TYPE_START = "st=>start: Start";
    private static final String NODE_TYPE_END = "e=>end: End";


    public ModelMap createModelMap(ModelMap model) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(LocaleContextHolder.getLocale());

        model.addAttribute("loggedinusername", "");
        model.addAttribute("dateFormatSymbols", dateFormatSymbols);
        model.addAttribute("loggedinuser", "");

        return model;
    }

    public String createObjectPayload(Object object){
        ObjectMapper om = new ObjectMapper();
        // support Java 8 date time apis
        om.registerModule(new JavaTimeModule());
        ObjectWriter ow = om.writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public  Map<String, Object> convertJsonStringToMap(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        // Convert JSON string to Map
        try {
             map = mapper.readValue(jsonString, Map.class);

        } catch (JsonProcessingException e) {
            return map;
        }
        return map;
    }

    public int getNextWorkflowStepOrder(Workflow workflow) {
        List<WorkflowStep> steps = workflow.getSteps();
        if (steps.isEmpty()) {
            return 1;
        }
        List<WorkflowStep> sorted = steps.stream().sorted(Comparator.comparing(WorkflowStep::getExecutionOrder)).toList();
        return sorted.get(sorted.size()-1).getExecutionOrder() + 1;
    }

    public String createFlowChartsCode(Workflow workflow){
        StringBuilder sb = new StringBuilder();
        List<WorkflowStep> steps = workflow.getSteps();
        List<WorkflowStep> sorted = steps.stream().sorted(Comparator.comparing(WorkflowStep::getExecutionOrder)).toList();
        sb.append(NODE_TYPE_START).append("\n");
        sb.append(NODE_TYPE_END).append("\n");
        generateNodes(sorted.stream().map(this::convertToFlowChartStep).toList(), sb);
        generateEdges(sorted.stream().map(this::convertToFlowChartStep).toList(), sb);
        return sb.toString();
    }


    public String createFlowChartsCode(WorkflowExecution execution){
        StringBuilder sb = new StringBuilder();
        List<WorkflowStepInstance> stepInstances = execution.getRunSteps();
        List<WorkflowStepInstance> sorted = stepInstances.stream().sorted(Comparator.comparing(WorkflowStepInstance::getExecutionOrder)).toList();
        sb.append(NODE_TYPE_START).append(" | past").append("\n");
        sb.append(NODE_TYPE_END).append(getStateColor(execution.getRunStatus())).append("\n");
        generateNodes(sorted.stream().map(this::convertToFlowChartStep).toList(), sb);
        generateEdges(sorted.stream().map(this::convertToFlowChartStep).toList(), sb);
        System.out.println(sb.toString());
        return sb.toString();
    }

    private void generateNodes(List<FlowChartStep> sorted, StringBuilder sb){
        for (FlowChartStep instance : sorted) {
            switch (instance.getControlStructure()) {
                case SIMPLE, SUB_WORKFLOW, DO_WHILE:
                    sb.append(instance.getName()).append(NODE_TYPE_OPERATION).append(getNodeText(instance)).append(getStateColor(instance.getRunStatus())).append("\n");
                    break;
                case SWITCH:
                    sb.append(instance.getName()).append(NODE_TYPE_PARALLEL).append(getNodeText(instance)).append(getStateColor(instance.getRunStatus())).append("\n");
                    for (DecisionCase decisionCase : instance.getDecisionCases()) {
                        sb.append(decisionCase.getName()).append(NODE_TYPE_OPERATION).append(getNodeText(decisionCase)).append(getStateColor(instance.getRunStatus())).append("\n");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void generateEdges(List<FlowChartStep> sorted, StringBuilder sb){
        for (int i = 0; i < sorted.size(); i++) {
            boolean last = i == sorted.size() - 1;
            if (i == 0) {
                if(last){
                    sb.append("st->").append(sorted.get(i).getName()).append("->e").append("\n");
                }else{
                    sb.append("st->").append(sorted.get(i).getName()).append("\n");
                    sb.append(sorted.get(i).getName()).append("->").append(sorted.get(i+1).getName()).append("\n");
                }
            }else{
                switch (sorted.get(i).getControlStructure()){
                    case SIMPLE, SUB_WORKFLOW:
                        if(last){
                            sb.append(sorted.get(i).getName()).append("->e").append("\n");
                        }else{
                            sb.append(sorted.get(i).getName()).append("->").append(sorted.get(i+1).getName()).append("\n");
                        }
                        break;
                    case SWITCH:
                        String para = sorted.get(i).getName();
                        List<DecisionCase> decisionCases = sorted.get(i).getDecisionCases();
                        for (int j=0; j<decisionCases.size(); j++) {
                            int path = j + 1;
                            if(last){
                                sb.append(para).append("(").append("path").append(path).append(", ").append(positions[j]).append(")").append("->").append(decisionCases.get(j).getName()).append("->e").append("\n");
                            }else{
                                sb.append(para).append("(").append("path").append(path).append(", ").append(positions[j]).append(")").append("->").append(decisionCases.get(j).getName()).append("->").append(sorted.get(i+1).getName()).append("\n");
                            }
                        }
                        break;
                    case DO_WHILE:
                        sb.append(sorted.get(i).getName()).append("(right)").append("->").append(sorted.get(i).getName()).append("\n");
                        if(last){
                            sb.append(sorted.get(i).getName()).append("->e").append("\n");
                        }else{
                            sb.append(sorted.get(i).getName()).append("->").append(sorted.get(i+1).getName()).append("\n");
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private String getStateColor(RunStatus runStatus){
        if (runStatus == null) {
            return "";
        }
        return switch (runStatus) {
            case COMPLETED -> " | past";
            case RUNNING -> " | current";
            default -> " | future";
        };
    }

    private String getNodeText(FlowChartStep step){
        return switch (step.getControlStructure()) {
            case SIMPLE -> "[Simple]\n----------------------\n" + step.getName();
            case SWITCH -> "SWITCH";
            case DO_WHILE -> "[While]\n----------------------\n" + step.getName();
            case SUB_WORKFLOW -> "[SubWorkflow]\n----------------------\n" + step.getName();
            default -> "";
        };
    }

    private String getNodeText(DecisionCase decisionCase){
        return "[Case]\n----------------------\n" + decisionCase.getName();
    }

    private FlowChartStep convertToFlowChartStep(WorkflowStep step){
        FlowChartStep flowChartStep = new FlowChartStep();
        flowChartStep.setUid(step.getUid());
        flowChartStep.setName(step.getName());
        flowChartStep.setWorkflowUid(step.getWorkflowUid());
        flowChartStep.setWorkflowName(step.getWorkflowName());
        flowChartStep.setDescription(step.getDescription());
        flowChartStep.setControlStructure(step.getControlStructure());
        flowChartStep.setExecutionOrder(step.getExecutionOrder());
        flowChartStep.setFunctionUid(step.getFunctionUid());
        flowChartStep.setSubWorkflowUid(step.getSubWorkflowUid());
        flowChartStep.setDecisionCases(step.getDecisionCases());
        flowChartStep.setSwitchCase(step.getSwitchCase());
        flowChartStep.setStopCondition(step.getStopCondition());
        flowChartStep.setCreated(step.getCreated());
        flowChartStep.setUpdated(step.getUpdated());
        return flowChartStep;
    }

    private FlowChartStep convertToFlowChartStep(WorkflowStepInstance instance){
        FlowChartStep flowChartStep = new FlowChartStep();
        flowChartStep.setUid(instance.getUid());
        flowChartStep.setName(instance.getName());
        flowChartStep.setWorkflowUid(instance.getWorkflowUid());
        flowChartStep.setRunStatus(instance.getRunStatus());
        flowChartStep.setWorkflowInstanceUid(instance.getWorkflowInstanceUid());
        flowChartStep.setDescription(instance.getDescription());
        flowChartStep.setControlStructure(instance.getControlStructure());
        flowChartStep.setExecutionOrder(instance.getExecutionOrder());
        flowChartStep.setFunctionUid(instance.getFunctionUid());
        flowChartStep.setSubWorkflowUid(instance.getSubWorkflowUid());
        flowChartStep.setWorkflowName(instance.getWorkflowName());
        flowChartStep.setDecisionCases(instance.getDecisionCases());
        flowChartStep.setSwitchCase(instance.getSwitchCase());
        flowChartStep.setStopCondition(instance.getStopCondition());
        flowChartStep.setCreated(instance.getCreated());
        flowChartStep.setUpdated(instance.getUpdated());
        flowChartStep.setFailureReason(instance.getFailureReason());
        flowChartStep.setScheduledTime(instance.getScheduledTime());
        flowChartStep.setStartTime(instance.getStartTime());
        flowChartStep.setEndTime(instance.getEndTime());
        return flowChartStep;
    }

}
