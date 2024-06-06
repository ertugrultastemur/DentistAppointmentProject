package com.dentai.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {

    private List<String> roles;

    private Long id;

    private Long branchId;
}
