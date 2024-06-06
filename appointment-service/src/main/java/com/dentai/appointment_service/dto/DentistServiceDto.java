package com.dentai.appointment_service.dto;

import com.dentai.appointment_service.model.Appointment;
import com.dentai.appointment_service.model.DentistService;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DentistServiceDto {

    private Long id;
    private String name;
    private Double price;
    private Integer durationMinute;
    private List<Long> appointmentIds;

    public static DentistServiceDto convert(DentistService dentistService){
        if (dentistService.getAppointments() == null){
            dentistService.setAppointments(new ArrayList<>());
        }
        return DentistServiceDto.builder()
                .id(dentistService.getId())
                .name(dentistService.getName())
                .price(dentistService.getPrice())
                .durationMinute(dentistService.getDurationMinute())
                .appointmentIds(dentistService.getAppointments().stream().map(Appointment::getId).collect(Collectors.toList()))
                .build();
    }
}
