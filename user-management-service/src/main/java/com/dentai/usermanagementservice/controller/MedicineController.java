package com.dentai.usermanagementservice.controller;

import com.dentai.usermanagementservice.dto.MedicineDto;
import com.dentai.usermanagementservice.service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDto> getMedicineById(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.getMedicineById(id));
    }

    @GetMapping("/getAllMedicines")
    public ResponseEntity<List<MedicineDto>> getAllMedicines() {
        return ResponseEntity.ok(medicineService.getAllMedicines());
    }

/*    @GetMapping("/getMedicinesByPrescriptionId/{prescriptionId}")
    public ResponseEntity<List<MedicineDto>> getAllMedicinesByPrescriptionId(@PathVariable Long prescriptionId) {
        return ResponseEntity.ok(medicineService.getAllMedicinesByPrescriptionId(prescriptionId));
    }*/

    @PostMapping("/addMedicine")
    public ResponseEntity<MedicineDto> addMedicine(@RequestBody MedicineDto medicineDto) {
        return ResponseEntity.ok(medicineService.createMedicine(medicineDto));
    }

    @PutMapping("/updateMedicine")
    public ResponseEntity<MedicineDto> updateMedicine(@RequestBody MedicineDto medicineDto) {
        return ResponseEntity.ok(medicineService.updateMedicine(medicineDto));
    }

    @DeleteMapping("/deleteMedicine/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok().build();
    }
}
