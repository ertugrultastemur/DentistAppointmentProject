package com.dentai.usermanagementservice.dto;

import com.dentai.usermanagementservice.model.Patient;
import com.dentai.usermanagementservice.model.PatientStatus;
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
    private PatientStatus status;
    private Integer completionPercentage;
    private List<String> performedOperations;
    private List<XRayDto> xRays;
    private List<PrescriptionDto> prescriptions;
    private Long userId;


    public static PatientDto convert(Patient patient) {
        if (patient.getPrescriptions() == null) {
            patient.setPrescriptions(List.of());
        }
        return PatientDto.builder()
                .id(patient.getId())
                .name(patient.getName())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .age(patient.getAge())
                .status(patient.getStatus())
                .completionPercentage(patient.getCompletionPercentage())
                .performedOperations(patient.getPerformedOperations())
                .prescriptions(patient.getPrescriptions().stream().map(PrescriptionDto::convert).collect(Collectors.toList()))
                .xRays(patient.getXRays().stream().map(XRayDto::convert).collect(Collectors.toList()))
                .userId(patient.getUser().getId())
                .build();
    }
}
