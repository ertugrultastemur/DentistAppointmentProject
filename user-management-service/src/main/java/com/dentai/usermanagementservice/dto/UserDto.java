package com.dentai.usermanagementservice.dto;

import com.dentai.usermanagementservice.model.Role;
import com.dentai.usermanagementservice.model.User;
import jakarta.transaction.Transactional;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private Integer age;
    private List<PatientDto> patients;
    private List<Role> roles;
    List<String> operations;
    private String profession;
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static UserDto convert(User user) {
        Logger logger = LoggerFactory.getLogger(UserDto.class);
        logger.trace("UserDto: convert started");
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .age(user.getAge())
                .patients(user.getPatients().stream().map(PatientDto::convert).collect(Collectors.toList()))
                .roles(user.getRoles())
                .operations(user.getOperations())
                .profession(user.getProfession())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .build();
    }
}
