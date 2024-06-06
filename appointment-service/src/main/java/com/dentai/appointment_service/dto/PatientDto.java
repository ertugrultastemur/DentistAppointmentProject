package com.dentai.appointment_service.dto;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDto {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Integer age;
    private List<String> performedOperations;
    private PatientStatus status;
    private Long userId;
    private Long createdBy;
    private Long updatedBy;


}
