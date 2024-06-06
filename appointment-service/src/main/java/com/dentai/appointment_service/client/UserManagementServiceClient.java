package com.dentai.appointment_service.client;

import com.dentai.appointment_service.dto.PatientDto;
import com.dentai.appointment_service.dto.UserDto;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "user-management-service", path = "/api/user")
public interface UserManagementServiceClient {

    @GetMapping("/users/{id}")
    ResponseEntity<UserDto> getUserById(@PathVariable Long id);

    @GetMapping("/users/getUserByEmail/{email}")
    ResponseEntity<UserDto> getUserByEmail(@PathVariable String email);

    @GetMapping("/users/getEmailById/{id}")
    ResponseEntity<String> getEmailById(@PathVariable Long id);

    @GetMapping("/users/getAllUsers")
    ResponseEntity<List<UserDto>> getAllUsers();

    @GetMapping("/patients/{id}")
    ResponseEntity<PatientDto> getPatientById(@PathVariable Long id);

    @GetMapping("/patients/getPatientByEmail/{email}")
    ResponseEntity<PatientDto> getPatientByEmail(@PathVariable String email);

    @GetMapping("/patients/getAllPatients")
    ResponseEntity<List<PatientDto>> getAllPatients();

    @PostMapping("/patients/addPatient")
    ResponseEntity<PatientDto> addPatient(PatientDto patientDto);

    @PutMapping(value = "/patients/updatePatient", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<PatientDto> updatePatient(@Nullable @RequestParam("images") List<MultipartFile> image, @ModelAttribute @RequestPart("patientDto") PatientDto patientDto);
}
