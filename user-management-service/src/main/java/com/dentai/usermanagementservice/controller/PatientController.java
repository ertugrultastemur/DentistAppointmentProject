package com.dentai.usermanagementservice.controller;

import com.dentai.usermanagementservice.dto.PatientDto;
import com.dentai.usermanagementservice.service.PatientService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/getAllPatientsByUserId/{userId}")
    public ResponseEntity<List<PatientDto>> getAllPatientsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(patientService.getAllPatientsByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/getPatientByEmail/{email}")
    public ResponseEntity<PatientDto> getPatientByEmail(@PathVariable String email) {
        return ResponseEntity.ok(patientService.getPatientByEmail(email));
    }
    @GetMapping("/getAllPatients")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PostMapping("/addPatient")
    public ResponseEntity<PatientDto> addPatient(@RequestBody PatientDto patientDto, HttpServletRequest request) {
        return ResponseEntity.ok(patientService.addPatient(patientDto, request));
    }

    @PutMapping(value = "/updatePatient", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PatientDto> updatePatient(@Nullable @RequestParam("images") List<MultipartFile> image, @ModelAttribute @RequestPart("patientDto") PatientDto patientDto, HttpServletRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(image, patientDto, request));
    }

    @DeleteMapping("/deletePatient/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id, HttpServletRequest request) {
        patientService.deletePatient(id, request);
        return ResponseEntity.ok().build();
    }

}
