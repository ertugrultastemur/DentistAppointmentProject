package com.dentai.appointment_service.dto;

import com.dentai.appointment_service.model.Appointment;
import com.dentai.appointment_service.model.DentistService;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAppointmentDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Integer age;
    private LocalDateTime appointmentDate;
    private List<String> dentistServices;
    private Long patientId;
    private Long dentistId;

    public static UpdateAppointmentDto convert(Appointment appointment){
        return UpdateAppointmentDto.builder()
                .id(appointment.getId())
                .name(appointment.getName())
                .address(appointment.getAddress())
                .age(appointment.getAge())
                .email(appointment.getEmail())
                .phoneNumber(appointment.getPhoneNumber())
                .appointmentDate(appointment.getAppointmentDate())
                .dentistServices(appointment.getDentistServices().stream().map(DentistService::getName).collect(Collectors.toList()))
                .patientId(appointment.getPatientId())
                .dentistId(appointment.getDentistId())
                .build();
    }
}
