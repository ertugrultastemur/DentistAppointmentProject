package com.dentai.authenticationservice.client;

import com.dentai.authenticationservice.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-management-service", path = "${feign.user-management-service.path}")
public interface UserManagementServiceClient {

    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUserById(@PathVariable @NotNull Long id);

    @GetMapping("/getUserByEmail/{email}")
    ResponseEntity<UserDto> getUserByEmail(@PathVariable @NotNull String email);

    @GetMapping("/getAllUsers")
    ResponseEntity<List<UserDto>> getAllUsers();
}
