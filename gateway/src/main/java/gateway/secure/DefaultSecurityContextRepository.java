package gateway.secure;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component()
public class DefaultSecurityContextRepository implements ServerSecurityContextRepository {

    private final String TOKEN_HEADER = "Authorization";

    private final String BEARER = "Bearer ";

    @Autowired
    private TokenAuthenticationManager tokenAuthenticationManager;

    private SecurityContext securityContext;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        this.securityContext = context;
        return Mono.empty();
    }

    public SecurityContext getSecurityContext() {
        return this.securityContext;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        List<String> headers = request.getHeaders().get(TOKEN_HEADER);
        if (!CollectionUtils.isEmpty(headers)) {
            String authorization = headers.get(0);
            if (StringUtils.isNotEmpty(authorization)) {
                String token = authorization.substring(BEARER.length());
                return tokenAuthenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(token, null)
                        ).onErrorReturn(UsernamePasswordAuthenticationToken.unauthenticated(token, null))
                        .flatMap(auth -> {
                            // Save the AuthenticationToken to SecurityContext
                            SecurityContextHolder.setContext(new SecurityContextImpl(auth));
                            // Return the AuthenticationToken if authenticated
                            return auth.isAuthenticated() ? Mono.just(SecurityContextHolder.getContext()) : Mono.empty();
                        });
            }
        }

        return Mono.empty();
    }
}