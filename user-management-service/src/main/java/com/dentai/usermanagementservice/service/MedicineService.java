package com.dentai.usermanagementservice.service;

import com.dentai.usermanagementservice.dto.MedicineDto;
import com.dentai.usermanagementservice.dto.PrescriptionDto;
import com.dentai.usermanagementservice.exception.MedicineNotFoundException;
import com.dentai.usermanagementservice.exception.PrescriptionNotFoundException;
import com.dentai.usermanagementservice.model.Medicine;
import com.dentai.usermanagementservice.model.Prescription;
import com.dentai.usermanagementservice.repository.MedicineRepository;
import com.dentai.usermanagementservice.repository.PrescriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;

    private final PrescriptionRepository prescriptionRepository;

    public MedicineService(MedicineRepository medicineRepository, PrescriptionRepository prescriptionRepository){
        this.medicineRepository = medicineRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public MedicineDto getMedicineById(Long id){
        return MedicineDto.convert(medicineRepository.findById(id).orElseThrow(() -> new MedicineNotFoundException(id)));
    }

    public List<MedicineDto> getAllMedicines(){
        return medicineRepository.findAll().stream().map(MedicineDto::convert).toList();
    }

/*    public List<MedicineDto> getAllMedicinesByPrescriptionId(Long prescriptionId){
        List<Medicine> medicines = medicineRepository.findMedicinesByPrescriptionId(prescriptionId).orElseThrow(() -> new PrescriptionNotFoundException(prescriptionId));
        return medicines.stream().map(MedicineDto::convert).toList();
    }*/

    @Transactional
    public MedicineDto createMedicine(MedicineDto medicineDto){
        List<Prescription> prescriptions = medicineDto.getPrescriptionIds().stream().map(prescriptionId -> prescriptionRepository.findById(prescriptionId).orElseThrow(() -> new PrescriptionNotFoundException(prescriptionId))).collect(Collectors.toList());
        Medicine medicine = Medicine.builder()
                .name(medicineDto.getName())
                .description(medicineDto.getDescription())
                .dosage(medicineDto.getDosage())
                .period(medicineDto.getPeriod())
                .instructions(medicineDto.getInstructions())
                .useCount(medicineDto.getUseCount())
                .boxQuantity(medicineDto.getBoxQuantity())
                .prescriptions(prescriptions)
                .isDeleted(false)
                .build();
        return MedicineDto.convert(medicineRepository.save(medicine));
    }

    @Transactional
    public MedicineDto updateMedicine(MedicineDto medicineDto){
        List<Prescription> prescriptions = medicineDto.getPrescriptionIds().stream().map(prescriptionId -> prescriptionRepository.findById(prescriptionId).orElseThrow(() -> new PrescriptionNotFoundException(prescriptionId))).collect(Collectors.toList());

        Medicine medicine = medicineRepository.findById(medicineDto.getId()).orElseThrow(() -> new MedicineNotFoundException(medicineDto.getId()));
        medicine.setName(medicineDto.getName());
        medicine.setDescription(medicineDto.getDescription());
        medicine.setDosage(medicineDto.getDosage());
        medicine.setPeriod(medicineDto.getPeriod());
        medicine.setInstructions(medicineDto.getInstructions());
        medicine.setUseCount(medicineDto.getUseCount());
        medicine.setBoxQuantity(medicineDto.getBoxQuantity());
        medicine.setPrescriptions(prescriptions);
        return MedicineDto.convert(medicineRepository.save(medicine));
    }

    @Transactional
    public void deleteMedicine(Long id){
        medicineRepository.softDeleteById(id);
    }
}
