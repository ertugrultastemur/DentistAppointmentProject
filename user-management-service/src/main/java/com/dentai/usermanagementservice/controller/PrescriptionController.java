package com.dentai.usermanagementservice.controller;

import com.dentai.usermanagementservice.dto.PrescriptionDto;
import com.dentai.usermanagementservice.service.PrescriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService){
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDto> getPrescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

/*    @GetMapping("/getPrescriptionsByPatientId/{patientId}")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatientId(patientId));
    }*/

    @GetMapping("/getAllPrescriptions")
    public ResponseEntity<List<PrescriptionDto>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
    }

    @PostMapping("/addPrescription")
    public ResponseEntity<PrescriptionDto> createPrescription(@RequestBody PrescriptionDto prescriptionDto, HttpServletRequest request) {
        return ResponseEntity.ok(prescriptionService.createPrescription(prescriptionDto, request));
    }

    @PutMapping("/updatePrescription")
    public ResponseEntity<PrescriptionDto> updatePrescription(@RequestBody PrescriptionDto prescriptionDto, HttpServletRequest request) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(prescriptionDto, request));
    }

    @DeleteMapping("/deletePrescription/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id, HttpServletRequest request) {
        prescriptionService.deletePrescriptionById(id, request);
        return ResponseEntity.ok().build();
    }
}
