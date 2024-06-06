package com.dentai.appointment_service.service;

import com.dentai.appointment_service.client.UserManagementServiceClient;
import com.dentai.appointment_service.dto.DentistServiceDto;
import com.dentai.appointment_service.dto.UserDto;
import com.dentai.appointment_service.exception.AppointmentNotFoundException;
import com.dentai.appointment_service.exception.DentistServiceNotFoundException;
import com.dentai.appointment_service.model.Appointment;
import com.dentai.appointment_service.model.DentistService;
import com.dentai.appointment_service.repository.AppointmentRepository;
import com.dentai.appointment_service.repository.DentistServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DentistServiceManager {

    private final DentistServiceRepository dentistServiceRepository;

    private final AppointmentRepository appointmentRepository;

    private final UserManagementServiceClient userManagementServiceClient;

    public DentistServiceManager(DentistServiceRepository dentistServiceRepository, AppointmentRepository appointmentRepository, UserManagementServiceClient userManagementServiceClient){
        this.dentistServiceRepository = dentistServiceRepository;
        this.appointmentRepository = appointmentRepository;
        this.userManagementServiceClient = userManagementServiceClient;
    }

    public DentistServiceDto getDentistServiceById(Long id){
        return DentistServiceDto.convert(dentistServiceRepository.findById(id).orElseThrow(() -> new DentistServiceNotFoundException(id)));
    }

    public List<DentistServiceDto> getDentistServicesByDentistId(Long dentistId){
        UserDto dentist = userManagementServiceClient.getUserById(dentistId).getBody();
        List<String> dentistServices = dentist.getOperations();
        List<DentistServiceDto> dentistServiceDtos = new ArrayList<>();
        dentistServices.forEach(d -> {
            DentistServiceDto newDentistServiceDto = dentistServiceRepository.findByName(d).map(DentistServiceDto::convert).orElseThrow(() -> new DentistServiceNotFoundException("Operation not found by name: " + d));
            dentistServiceDtos.add(newDentistServiceDto);
        });
        return dentistServiceDtos;
    }

    public List<DentistServiceDto> getAllDentistServices(){
        return dentistServiceRepository.findAll().stream().map(DentistServiceDto::convert).collect(Collectors.toList());
    }

    @Transactional
    public DentistServiceDto createDentistService(DentistServiceDto dentistServiceDto){
        DentistService dentistService = DentistService.builder()
                .name(dentistServiceDto.getName())
                .price(dentistServiceDto.getPrice())
                .durationMinute(dentistServiceDto.getDurationMinute())
                .isDeleted(false)
                .build();

        return DentistServiceDto.convert(dentistServiceRepository.save(dentistService));
    }

    @Transactional
    public DentistServiceDto updateDentistService(DentistServiceDto dentistServiceDto){
        DentistService dentistService = dentistServiceRepository.findById(dentistServiceDto.getId()).orElseThrow(() -> new DentistServiceNotFoundException(dentistServiceDto.getId()));

        dentistService.setName(dentistServiceDto.getName());
        dentistService.setPrice(dentistServiceDto.getPrice());
        dentistService.setDurationMinute(dentistServiceDto.getDurationMinute());

        return DentistServiceDto.convert(dentistService);
    }

    @Transactional
    public void deleteDentistService(Long id){
        dentistServiceRepository.softDeleteById(id);
    }
}
