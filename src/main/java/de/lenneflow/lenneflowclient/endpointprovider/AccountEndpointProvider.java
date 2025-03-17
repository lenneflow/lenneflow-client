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
public class AccountEndpointProvider {


    @Value("${lenneflow.root.url}")
    private String rootUrl;

    @Value("${lenneflow.function.root.url}")
    private String accountRootUrl;

    @Value("${resource.account.create}")
    private String createAccountPath;

    @Value("${resource.account.find}")
    private String findAccountPath;

    @Value("${resource.account.delete}")
    private String deleteAccountPath;

    @Value("${resource.account.find-all}")
    private String findAllAccountsPath;

    @Value("${resource.account.user-token}")
    private String userTokenPath;

    @Value("${resource.account.update-password}")
    private String updatePasswordPath;

}
