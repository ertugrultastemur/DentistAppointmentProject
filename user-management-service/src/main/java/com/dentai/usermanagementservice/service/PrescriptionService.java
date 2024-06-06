package com.dentai.usermanagementservice.service;

import com.dentai.usermanagementservice.dto.PrescriptionDto;
import com.dentai.usermanagementservice.exception.InvalidUserDataException;
import com.dentai.usermanagementservice.exception.MedicineNotFoundException;
import com.dentai.usermanagementservice.exception.PrescriptionNotFoundException;
import com.dentai.usermanagementservice.exception.UserNotFoundException;
import com.dentai.usermanagementservice.model.Medicine;
import com.dentai.usermanagementservice.model.Patient;
import com.dentai.usermanagementservice.model.Prescription;
import com.dentai.usermanagementservice.repository.MedicineRepository;
import com.dentai.usermanagementservice.repository.PatientRepository;
import com.dentai.usermanagementservice.repository.PrescriptionRepository;
import com.dentai.usermanagementservice.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    private final PatientRepository patientRepository;

    private final MedicineRepository medicineRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, PatientRepository patientRepository, MedicineRepository medicineRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
    }

    public PrescriptionDto getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id).map(PrescriptionDto::convert).orElseThrow(() -> new PrescriptionNotFoundException(id));
    }

/*    public List<PrescriptionDto> getPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId).stream().map(PrescriptionDto::convert).collect(Collectors.toList());
    }*/

    public List<PrescriptionDto> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream().map(PrescriptionDto::convert).collect(Collectors.toList());
    }

    @Transactional
    public PrescriptionDto createPrescription(PrescriptionDto prescriptionDto, HttpServletRequest request) {

        if (prescriptionDto.getPatientId() == null) {
            throw new InvalidUserDataException("Patient is required");
        }
        Patient patient = patientRepository.findById(prescriptionDto.getPatientId()).orElseThrow(() -> new UserNotFoundException(prescriptionDto.getPatientId()));

        List<Medicine> medicines = prescriptionDto.getMedicines().stream().map(medicineDto -> {
            return medicineRepository.findById(medicineDto.getId()).orElseThrow(() -> new MedicineNotFoundException(medicineDto.getId()));
        }).collect(Collectors.toList());
        Prescription prescription = Prescription.builder()
                .name(prescriptionDto.getName())
                .description(prescriptionDto.getDescription())
                .patient(patient)
                .medicines(medicines)
                .createdBy(HeaderUtil.getUserId(request))
                .isDeleted(false)
                .build();

        return PrescriptionDto.convert(prescriptionRepository.save(prescription));
    }

    @Transactional
    public PrescriptionDto updatePrescription(PrescriptionDto prescriptionDto, HttpServletRequest request) {
        Patient patient = patientRepository.findById(prescriptionDto.getPatientId()).orElseThrow(() -> new UserNotFoundException(prescriptionDto.getPatientId()));
        Prescription prescription = prescriptionRepository.findById(prescriptionDto.getId()).orElseThrow(() -> new UserNotFoundException(prescriptionDto.getId()));

        List<Medicine> medicines = prescriptionDto.getMedicines().stream().map(medicineDto -> {
            return medicineRepository.findById(medicineDto.getId()).orElseThrow(() -> new MedicineNotFoundException(medicineDto.getId()));
        }).collect(Collectors.toList());
        prescription.setName(prescriptionDto.getName());
        prescription.setDescription(prescriptionDto.getDescription());
        prescription.setPatient(patient);
        prescription.setMedicines(medicines);
        prescription.setUpdatedBy(HeaderUtil.getUserId(request));
        prescription.setUpdatedDate(LocalDateTime.now());

        return PrescriptionDto.convert(prescriptionRepository.save(prescription));
    }

    @Modifying
    @Transactional
    public void deletePrescriptionById(Long id, HttpServletRequest request) {
        prescriptionRepository.softDeleteById(id, HeaderUtil.getUserId(request));
    }
}
