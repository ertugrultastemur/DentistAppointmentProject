package com.dentai.usermanagementservice.dto;

import com.dentai.usermanagementservice.model.Prescription;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionDto {

    private Long id;
    private String name;
    private String description;
    private List<MedicineDto> medicines;
    private Long patientId;
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    public static PrescriptionDto convert(Prescription prescription) {
        if (prescription.getMedicines() == null) {
            prescription.setMedicines(List.of());
        }
        return PrescriptionDto.builder()
                .id(prescription.getId())
                .name(prescription.getName())
                .description(prescription.getDescription())
                .medicines(prescription.getMedicines().stream().map(MedicineDto::convert).toList())
                .patientId(prescription.getPatient().getId())
                .createdBy(prescription.getCreatedBy())
                .updatedBy(prescription.getUpdatedBy())
                .createdDate(prescription.getCreatedDate())
                .updatedDate(prescription.getUpdatedDate())
                .build();
    }
}
