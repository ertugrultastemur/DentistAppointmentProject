package com.dentai.appointment_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDentistServiceDto {
    private Long id;
    private String name;
    private Double price;
    private Integer durationMinute;


}
