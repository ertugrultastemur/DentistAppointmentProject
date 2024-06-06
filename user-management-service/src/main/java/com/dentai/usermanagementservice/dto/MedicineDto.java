package com.dentai.usermanagementservice.dto;

import com.dentai.usermanagementservice.model.Medicine;
import com.dentai.usermanagementservice.model.Prescription;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicineDto {

    private Long id;
    private String name;
    private String description;
    private Integer dosage;
    private Integer period;
    private String instructions;
    private Integer useCount;
    private Integer boxQuantity;
    private List<Long> prescriptionIds;


    public static MedicineDto convert(Medicine medicine) {
        return MedicineDto.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .description(medicine.getDescription())
                .dosage(medicine.getDosage())
                .period(medicine.getPeriod())
                .instructions(medicine.getInstructions())
                .useCount(medicine.getUseCount())
                .boxQuantity(medicine.getBoxQuantity())
                .prescriptionIds(medicine.getPrescriptions().stream().map(Prescription::getId).collect(Collectors.toList()))
                .build();
    }
}
