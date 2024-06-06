package com.dentai.usermanagementservice.service;

import com.dentai.usermanagementservice.dto.PatientDto;
import com.dentai.usermanagementservice.dto.XRayDto;
import com.dentai.usermanagementservice.exception.InvalidUserDataException;
import com.dentai.usermanagementservice.exception.UserNotFoundException;
import com.dentai.usermanagementservice.model.*;
import com.dentai.usermanagementservice.repository.PatientRepository;
import com.dentai.usermanagementservice.repository.PrescriptionRepository;
import com.dentai.usermanagementservice.repository.UserRepository;
import com.dentai.usermanagementservice.util.HeaderUtil;
import com.dentai.usermanagementservice.util.ModelMapperService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.hibernate.type.SerializationException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    private final UserRepository userRepository;

    private final ModelMapperService modelMapperService;

    private final XRayService xRayService;

    private final PrescriptionRepository prescriptionRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository, ModelMapperService modelMapperService, XRayService xRayService, PrescriptionRepository prescriptionRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.modelMapperService = modelMapperService;
        this.xRayService = xRayService;
        this.prescriptionRepository = prescriptionRepository;
    }

    public PatientDto getPatientById(Long id) {
        return PatientDto.convert(patientRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Patient not found by id: " + id)));
    }

    public PatientDto getPatientByEmail(String email) {
        if (patientRepository.findByEmail(email).isEmpty()) {
            return null;
        }
        return PatientDto.convert(patientRepository.findByEmail(email).get());
    }

    public List<PatientDto> getAllPatientsByUserId(Long userId) {
        List<Patient> patients = patientRepository.findAllByUserId(userId).orElseThrow(() -> new UserNotFoundException("Patients not found by user id: " + userId));
        return patients.stream().map(PatientDto::convert).toList();
    }
    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream().map(PatientDto::convert).collect(Collectors.toList());
    }

    @Transactional
    public PatientDto addPatient(PatientDto patientDto, HttpServletRequest request) {
        User user = this.userRepository.findById(patientDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found by id: " + patientDto.getUserId()));
        Patient patient = Patient.builder()
                .name(patientDto.getName())
                .email(patientDto.getEmail())
                .phoneNumber(patientDto.getPhoneNumber())
                .address(patientDto.getAddress())
                .age(patientDto.getAge())
                .status(PatientStatus.IN_PROGRESS)
                .completionPercentage(0)
                .performedOperations(new ArrayList<>())
                .xRays(new ArrayList<>())
                .user(user)
                .createdBy(HeaderUtil.getUserId(request))
                .build();
        return PatientDto.convert(patientRepository.save(patient));
    }

    @Transactional
    public PatientDto updatePatient(List<MultipartFile> images, PatientDto patientDto, HttpServletRequest request) {
        User user = this.userRepository.findById(patientDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found by id: " + patientDto.getUserId()));
        Patient patient = patientRepository.findById(patientDto.getId()).orElseThrow(() -> new UserNotFoundException("Patient not found by id: " + patientDto.getId()));
        List<Prescription> prescriptions = patient.getPrescriptions().stream().map(prescription -> {
            return prescriptionRepository.findById(prescription.getId()).orElseThrow(() -> new UserNotFoundException("Prescription not found by id: " + prescription.getId()));
        }).collect(Collectors.toList());
        patient.setName(patientDto.getName());
        patient.setAddress(patientDto.getAddress());
        patient.setAge(patientDto.getAge());
        patient.setEmail(patientDto.getEmail());
        patient.setPhoneNumber(patientDto.getPhoneNumber());
        patient.setPerformedOperations(patientDto.getPerformedOperations());
        patient.setPrescriptions(prescriptions);
        if (patientDto.getCompletionPercentage() != null && (patientDto.getCompletionPercentage() > 100 || patientDto.getCompletionPercentage() < 0)) {
            throw new UserNotFoundException("Completion percentage must be between 0 and 100");
        } else if (patientDto.getCompletionPercentage() != null && patientDto.getCompletionPercentage() == 100) {
            patient.setCompletionPercentage(patientDto.getCompletionPercentage());
            patient.setStatus(PatientStatus.COMPLETED);
        } else if (patientDto.getCompletionPercentage() != null) {
            patient.setCompletionPercentage(patientDto.getCompletionPercentage());
            patient.setStatus(PatientStatus.IN_PROGRESS);
        }
        if (images != null && !images.isEmpty()) {
            try {
                XRayDto xRayDto = new XRayDto();
                xRayDto.setPatientId(patient.getId());
                xRayService.addXRay(xRayDto, images, request);
            } catch (IOException | SQLException e) {
                throw new InvalidUserDataException(e.getMessage());
            }
        }
        patient.setUpdatedBy(HeaderUtil.getUserId(request));
        patient.setUser(user);
        patientRepository.save(patient);
        return PatientDto.convert(patient);
    }

    @Transactional
    public void deletePatient(Long id, HttpServletRequest request) {
        patientRepository.softDeleteById(id, HeaderUtil.getUserId(request));
    }
}
