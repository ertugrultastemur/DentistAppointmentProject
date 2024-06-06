package com.dentai.usermanagementservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "authentication-service", path = "/api/auth/member")
public interface AuthenticationServiceClient {

    @PostMapping("/passwordEncode/{password}")
    ResponseEntity<String> passwordEncoder(@PathVariable String password);
}
