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
public class FunctionEndpointProvider {


    @Value("${lenneflow.root.url}")
    private String rootUrl;

    @Value("${lenneflow.function.root.url}")
    private String functionRootUrl;

    @Value("${resource.function.json-schema.create}")
    private String createJsonSchemaPath;

    @Value("${resource.function.json-schema.find}")
    private String findJsonSchemaPath;

    @Value("${resource.function.json-schema.find-all}")
    private String findAllJsonSchemaPath;

    @Value("${resource.function.create}")
    private String createFunctionPath;

    @Value("${resource.function.find}")
    private String findFunctionPath;

    @Value("${resource.function.delete}")
    private String deleteFunctionPath;

    @Value("${resource.function.find-all}")
    private String findAllFunctionsList;

    @Value("${resource.function.json-schema.delete}")
    private String deleteJsonSchemaPath;

    @Value("${resource.function.json-schema.find-all}")
    private String findJsonSchemaListPath;

    @Value("${resource.function.deploy}")
    private String deployFunctionPath;

    @Value("${resource.function.undeploy}")
    private String unDeployFunctionPath;

}
