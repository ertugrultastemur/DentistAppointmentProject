package com.dentai.gatewayservice.config;

import com.dentai.gatewayservice.dto.ValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthorizationFilter implements GatewayFilter {
    private final RestTemplate template;

    public AuthorizationFilter(RestTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());

        List<String> role = validateToken(token).getBody().getRoles();
        Long userId = validateToken(token).getBody().getId();

        String requestPath = request.getPath().toString();
        if (requestPath.startsWith("/api/user")) {
            if (!role.contains("ROLE_DENTIST")) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                log.info("Unauthorized access: "+ role + HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        } else if (requestPath.startsWith("/api/auth")) {
            if (!role.contains("ROLE_ADMIN")) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                log.info("Unauthorized access: "+ role + HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        } else if (requestPath.startsWith("/api/appointment")) {
            if (!role.contains("ROLE_DENTIST")) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                log.info("Unauthorized access: "+ role + HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }else {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        ServerHttpRequest mutableRequest = exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.add("X-USER-ID", userId.toString()))
                .build();
        return chain.filter(exchange.mutate().request(mutableRequest).build());
    }

    ResponseEntity<ValidationResponse> validateToken(String token) {
        return template.getForEntity("http://authentication-service:8989/api/auth/auth/validate?token=" + token, ValidationResponse.class);
    }

}


