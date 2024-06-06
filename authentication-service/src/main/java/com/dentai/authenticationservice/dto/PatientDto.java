package com.dentai.authenticationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private List<XRayDto> xRays;
    private Long userId;
}
