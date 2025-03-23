package de.lenneflow.lenneflowclient.config;


import de.lenneflow.lenneflowclient.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomAuthenticationProvider authProvider;

    public WebSecurityConfig(CustomAuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/img/**", "/css/**", "/js/**", "/scss/**", "/vendor/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/**").authenticated())
                // All other requests must be authenticated
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)).build();
    }

}