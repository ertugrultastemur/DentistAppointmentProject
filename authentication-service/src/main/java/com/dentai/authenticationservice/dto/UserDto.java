package com.dentai.authenticationservice.dto;

import com.dentai.authenticationservice.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private List<Role> roles;
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


}
