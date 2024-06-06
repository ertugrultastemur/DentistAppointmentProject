package com.dentai.authenticationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationResponseDto {

    private List<String> roles;

    private Long id;

    boolean isValid;
}
