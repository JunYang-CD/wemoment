package gateway.secure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
public class GatewayGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    DefaultSecurityContextRepository securityContextRepository;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI uri = exchange.getRequest().getURI();
        log.info(uri.getPath());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null) {
            Authentication auth = securityContext.getAuthentication();
            if (auth != null && auth.getPrincipal() != null) {
                exchange.getRequest().mutate().headers(headers -> {
                    headers.set("uuid", auth.getPrincipal().toString());
                });
            }
        }
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        // Before NettyRoutingFilter since this routes certain http requests
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

}