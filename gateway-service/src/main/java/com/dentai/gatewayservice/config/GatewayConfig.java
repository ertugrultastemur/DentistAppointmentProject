package com.dentai.gatewayservice.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.CorsRegistry;

@Configuration
public class GatewayConfig {
    private final AuthenticationFilter authenticationFilter = new AuthenticationFilter(new RestTemplate());


    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }




    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthorizationFilter authorizationFilter, RouteValidator routeValidator) {
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        GatewayFilter filter = authenticationFilter.apply(config);
        return builder.routes()
                .route("user-management-service", r -> r.path("/api/user/**")
                        .filters(f -> f
                                .filter(filter)
                                .filter(authorizationFilter))
                        .uri("lb://user-management-service"))
                .route("authentication-service", r -> r.path("/api/auth/**")
                        .filters(f -> f
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                                .filter(routeValidator))
                        .uri("lb://authentication-service"))
                .route("appointment-service", r -> r.path("/api/appointment/**")
                        .filters(f -> f
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                                .filter(routeValidator))
                        .uri("lb://appointment-service"))
                .route("dentist-info-service", r -> r.path("/api/dentistInfo/**")
                        .filters(f -> f
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                                .filter(routeValidator))
                        .uri("lb://user-management-service"))
                .build();
    }


}