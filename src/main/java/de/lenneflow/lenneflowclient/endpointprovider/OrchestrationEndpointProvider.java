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
public class OrchestrationEndpointProvider {

    @Value("${lenneflow.orchestration.root.url}")
    private String orchestrationRootUrl;

    @Value("${resource.orchestration.workflow.start}")
    private String startWorkflowPath;

    @Value("${resource.orchestration.workflow.with-input-id.start}")
    private String startWorkflowWithInputIdPath;

    @Value("${resource.orchestration.workflow.with-input-payload.start}")
    private String startWorkflowWithInputPayloadPath;

    @Value("${resource.orchestration.workflow.stop}")
    private String stopWorkflowPath;

    @Value("${resource.orchestration.workflow.resume}")
    private String resumeWorkflowPath;

    @Value("${resource.orchestration.workflow.pause}")
    private String pauseWorkflowPath;

    @Value("${resource.orchestration.workflow.state}")
    private String workflowStatePath;

    @Value("${resource.orchestration.workflow.instances.find-all}")
    public String findAllWorkflowInstancesPath;

    @Value("${resource.orchestration.workflow.instances.delete}")
    public String deleteWorkflowInstancesPath;
}
