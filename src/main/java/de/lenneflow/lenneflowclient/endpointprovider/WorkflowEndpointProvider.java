package de.lenneflow.lenneflowclient.endpointprovider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class WorkflowEndpointProvider {
    @Value("${lenneflow.root.url}")
    private String rootUrl;

    @Value("${lenneflow.workflow.root.url}")
    private String workflowRootUrl;

    @Value("${resource.workflow.json-schema.create}")
    private String createJsonSchemaPath;

    @Value("${resource.workflow.json-schema.find}")
    private String findJsonSchemaPath;

    @Value("${resource.workflow.json-schema.find-all}")
    private String findAllJsonSchemaPath;

    @Value("${resource.workflow.json-schema.delete}")
    private String deleteJsonSchemaPath;

    @Value("${resource.workflow.json-schema.find-all}")
    private String findJsonSchemaListPath;

    @Value("${resource.workflow.create}")
    private String createWorkflowPath;

    @Value("${resource.workflow.find}")
    private String findWorkflowPath;

    @Value("${resource.workflow.delete}")
    private String deleteWorkflowPath;

    @Value("${resource.workflow.find-all}")
    private String findAllWorkflowsPath;

    @Value("${resource.workflow.step.simple.create}")
    private String createSimpleWorkflowStepPath;

    @Value("${resource.workflow.step.switch.create}")
    private String createSwitchWorkflowStepPath;

    @Value("${resource.workflow.step.while.create}")
    private String createWhileWorkflowStepPath;

    @Value("${resource.workflow.step.sub-workflow.create}")
    private String createSubWorkflowStepPath;

    @Value("${resource.workflow.step.find}")
    private String findWorkflowStepPath;

    @Value("${resource.workflow.step.find-all}")
    private String findAllWorkflowStepsPath;

    @Value("${resource.workflow.step.delete}")
    private String deleteWorkflowStepPath;


}
