package com.dentai.appointment_service.service;

import com.dentai.appointment_service.client.UserManagementServiceClient;
import com.dentai.appointment_service.dto.*;
import com.dentai.appointment_service.exception.AppointmentNotFoundException;
import com.dentai.appointment_service.exception.DentistServiceNotFoundException;
import com.dentai.appointment_service.model.Appointment;
import com.dentai.appointment_service.model.DentistService;
import com.dentai.appointment_service.repository.AppointmentRepository;
import com.dentai.appointment_service.repository.DentistServiceRepository;
import com.dentai.appointment_service.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final UserManagementServiceClient userManagementServiceClient;

    private final DentistServiceRepository dentistServiceRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, UserManagementServiceClient userManagementServiceClient, DentistServiceRepository dentistServiceRepository){
        this.appointmentRepository = appointmentRepository;
        this.userManagementServiceClient = userManagementServiceClient;
        this.dentistServiceRepository = dentistServiceRepository;
    }

    public List<AppointmentDto> getAllAppointments(){
        return appointmentRepository.findAll().stream().map(AppointmentDto::convert).toList();
    }

    public AppointmentDto getAppointmentById(Long id){
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        return AppointmentDto.builder()
                .name(appointment.getName())
                .email(appointment.getEmail())
                .address(appointment.getAddress())
                .age(appointment.getAge())
                .phoneNumber(appointment.getPhoneNumber())
                .appointmentDate(appointment.getAppointmentDate())
                .dentistId(appointment.getDentistId())
                .build();
    }

    @Transactional
    public void createAppointment(UpdateAppointmentDto appointmentDto, HttpServletRequest request){
        List<DentistService> dentistServices = appointmentDto.getDentistServices().stream().map(name -> dentistServiceRepository.findByName(name).orElseThrow(() -> new DentistServiceNotFoundException(name))).toList();
        PatientDto patient = userManagementServiceClient.getPatientByEmail(appointmentDto.getEmail()).getBody();
        List<String> performedOperations = new ArrayList<>(appointmentDto.getDentistServices());
        if(patient!=null){
            if (dentistServiceRepository.findByName(appointmentDto.getName()).isPresent()) {
                patient.getPerformedOperations().addAll(appointmentDto.getDentistServices());

            }
            appointmentDto.setPatientId(patient.getId());
            Appointment appointment = Appointment.builder()
                    .name(appointmentDto.getName())
                    .email(appointmentDto.getEmail())
                    .phoneNumber(appointmentDto.getPhoneNumber())
                    .address(appointmentDto.getAddress())
                    .age(appointmentDto.getAge())
                    .dentistServices(dentistServices)
                    .appointmentDate(appointmentDto.getAppointmentDate())
                    .dentistId(appointmentDto.getDentistId())
                    .createdBy(HeaderUtil.getUserId(request))
                    .isDeleted(false)
                    .build();

            UserDto dentist = userManagementServiceClient.getUserById(appointmentDto.getDentistId()).getBody();
            PatientDto patientDto= PatientDto.builder()
                    .id(appointmentDto.getPatientId())
                    .name(appointment.getName())
                    .email(appointment.getEmail())
                    .address(appointment.getAddress())
                    .age(appointment.getAge())
                    .phoneNumber(appointment.getPhoneNumber())
                    .status(PatientStatus.IN_PROGRESS)
                    .performedOperations(patient.getPerformedOperations())
                    .createdBy(HeaderUtil.getUserId(request))
                    .userId(dentist.getId())
                    .build();
            patientDto = userManagementServiceClient.updatePatient(null, patientDto).getBody();
            appointment.setPatientId(patientDto.getId());
            appointmentRepository.save(appointment);
            return;
        }
        Appointment appointment = Appointment.builder()
                .name(appointmentDto.getName())
                .email(appointmentDto.getEmail())
                .phoneNumber(appointmentDto.getPhoneNumber())
                .address(appointmentDto.getAddress())
                .age(appointmentDto.getAge())
                .dentistServices(dentistServices)
                .appointmentDate(appointmentDto.getAppointmentDate())
                .dentistId(appointmentDto.getDentistId())
                .createdBy(HeaderUtil.getUserId(request))
                .isDeleted(false)
                .build();
        UserDto dentist = userManagementServiceClient.getUserById(appointmentDto.getDentistId()).getBody();
        PatientDto patientDto= PatientDto.builder()
                        .id(appointmentDto.getPatientId())
                        .name(appointment.getName())
                        .email(appointment.getEmail())
                        .address(appointment.getAddress())
                        .age(appointment.getAge())
                        .phoneNumber(appointment.getPhoneNumber())
                        .performedOperations(performedOperations)
                        .status(PatientStatus.IN_PROGRESS)
                        .createdBy(HeaderUtil.getUserId(request))
                        .userId(dentist.getId())
                        .build();
        patientDto = userManagementServiceClient.addPatient(patientDto).getBody();
        appointment.setPatientId(patientDto.getId());
        appointmentRepository.save(appointment);
    }

    @Transactional
    public AppointmentDto updateAppointment(UpdateAppointmentDto appointmentDto, HttpServletRequest request){
        UserDto dentist = userManagementServiceClient.getUserById(appointmentDto.getDentistId()).getBody();
        Appointment appointment = appointmentRepository.findById(appointmentDto.getId()).orElseThrow(() -> new AppointmentNotFoundException(appointmentDto.getId()));
        PatientDto patientDto = userManagementServiceClient.getPatientById(appointment.getPatientId()).getBody();

        List<DentistService> dentistServices = appointmentDto.getDentistServices().stream().map(name -> dentistServiceRepository.findByName(name).orElseThrow(() -> new DentistServiceNotFoundException(name))).toList();

        appointment.setName(appointmentDto.getName());
        appointment.setEmail(appointmentDto.getEmail());
        appointment.setPhoneNumber(appointmentDto.getPhoneNumber());
        appointment.setAddress(appointmentDto.getAddress());
        appointment.setAge(appointmentDto.getAge());
        appointment.setUpdatedBy(HeaderUtil.getUserId(request));
        appointment.setDentistId(dentist.getId());
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setDentistServices(dentistServices);

        patientDto.setName(patientDto.getName());
        patientDto.setEmail(appointment.getEmail());
        patientDto.setAddress(appointment.getAddress());
        patientDto.setAge(appointment.getAge());
        patientDto.setPhoneNumber(appointment.getPhoneNumber());
        patientDto.setUpdatedBy(HeaderUtil.getUserId(request));
        patientDto.setUserId(dentist.getId());

        userManagementServiceClient.updatePatient(null, patientDto);

        return AppointmentDto.convert(appointmentRepository.save(appointment));
    }

    @Transactional
    public void deleteAppointment(Long id, HttpServletRequest request){
        appointmentRepository.softDeleteById(id, HeaderUtil.getUserId(request));
    }
}
