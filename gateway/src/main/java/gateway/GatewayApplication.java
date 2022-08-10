package gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import gateway.secure.GatewayGlobalFilter;

@ComponentScan(basePackages = {"gateway/secure", "gateway.controller", "gateway.config"})
@SpringBootApplication
@EnableEurekaClient
@EnableAutoConfiguration
@RestController
@EnableFeignClients
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/moments/**")
                        .uri("lb://moment-service")
                )
                .route(r -> r.path("/user/**")
                        .uri("lb://user-service"))
                .route(r -> r.path("/login")
                        .uri("forward:/gateway/login")
                )
                .route(r ->r.path("/notification/**")
                        .uri("lb://notification-service")
                )
                .build();
    }
}
