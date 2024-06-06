package com.dentai.appointment_service.dto;

import com.dentai.appointment_service.model.Appointment;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import org.apache.hc.core5.util.TimeValue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Integer age;
    private LocalDateTime appointmentDate;
    private List<DentistServiceDto> dentistServices;
    private Long patientId;
    private Long dentistId;

    public static AppointmentDto convert(Appointment appointment){
        return AppointmentDto.builder()
                .id(appointment.getId())
                .name(appointment.getName())
                .address(appointment.getAddress())
                .age(appointment.getAge())
                .email(appointment.getEmail())
                .phoneNumber(appointment.getPhoneNumber())
                .appointmentDate(appointment.getAppointmentDate())
                .dentistServices(appointment.getDentistServices().stream().map(DentistServiceDto::convert).collect(Collectors.toList()))
                .patientId(appointment.getPatientId())
                .dentistId(appointment.getDentistId())
                .build();
    }

}
