package com.dentai.usermanagementservice.controller;

import com.dentai.usermanagementservice.dto.UserDto;
import com.dentai.usermanagementservice.service.DentistInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/dentistInfo")
@RestController
public class DentistInfoController {

    private final DentistInfoService dentistInfoService;

    public DentistInfoController(DentistInfoService dentistInfoService) {
        this.dentistInfoService = dentistInfoService;
    }

    @GetMapping("/getAllDentists")
    public ResponseEntity<List<UserDto>> getAllDentists() {
        return ResponseEntity.ok(dentistInfoService.getAllDentists());
    }

}
