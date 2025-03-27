package de.lenneflow.lenneflowclient.security;

import de.lenneflow.lenneflowclient.endpointprovider.AccountEndpointProvider;
import de.lenneflow.lenneflowclient.model.AccountUser;
import de.lenneflow.lenneflowclient.util.RestUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final RestUtil restUtil;
    private final AccountEndpointProvider accountEndpointProvider;

    public CustomAuthenticationProvider(RestUtil restUtil, AccountEndpointProvider accountEndpointProvider) {
        this.restUtil = restUtil;
        this.accountEndpointProvider = accountEndpointProvider;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();
        AccountUser foundUser = restUtil.getForObject(accountEndpointProvider.getAccountRootUrl() + accountEndpointProvider.getFindAccountPath().replace("{uid}", uid), AccountUser.class);
        return authenticateAgainstThirdPartyAndGetAuthentication(name, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private static UsernamePasswordAuthenticationToken authenticateAgainstThirdPartyAndGetAuthentication(String name, String password) {
        final List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
        final UserDetails principal = new User(name, password, grantedAuths);
        return new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
    }
}
