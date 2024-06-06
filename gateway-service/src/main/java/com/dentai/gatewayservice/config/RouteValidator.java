package com.dentai.gatewayservice.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouteValidator implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        isSecured.test(exchange.getRequest());

        return chain.filter(exchange);
    }

    public static final List<String> openEndpoints = List.of(
            "/api/auth/auth/register",
            "/api/auth/auth/login",
            "/api/auth/auth/refresh",
            "/eureka"
    );



    private final Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request
                            .getURI()
                            .getPath()
                            .contains(uri));


}