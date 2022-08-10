package gateway.secure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Autowired
    private DefaultAuthorizationManager defaultAuthorizationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private TokenAuthenticationManager tokenAuthenticationManager;

    @Autowired
    private DefaultSecurityContextRepository defaultSecurityContextRepository;

    @Autowired
    private DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint;

    @Autowired
    private DefaultAccessDeniedHandler defaultAccessDeniedHandler;


    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .securityContextRepository(defaultSecurityContextRepository)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/login").permitAll()
                        .pathMatchers("/user/add").permitAll()
                        .anyExchange().access(defaultAuthorizationManager)
                )
                .exceptionHandling()
                .accessDeniedHandler(defaultAccessDeniedHandler)
                .authenticationEntryPoint(defaultAuthenticationEntryPoint)
        ;
        return http.build();
    }

    @Bean("UserDetailsRepositoryReactiveAuthenticationManager")
    ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsServiceImpl);
    }
}
