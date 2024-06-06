package com.dentai.appointment_service.controller;

import com.dentai.appointment_service.dto.DentistServiceDto;
import com.dentai.appointment_service.service.DentistServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment/dentistServices")
public class DentistServiceController {

    private final DentistServiceManager dentistServiceManager;

    public DentistServiceController(DentistServiceManager dentistServiceManager){
        this.dentistServiceManager = dentistServiceManager;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistServiceDto> getDentistServiceById(@PathVariable Long id){
        return ResponseEntity.ok(dentistServiceManager.getDentistServiceById(id));
    }

    @GetMapping("/getAllDentistServices")
    public ResponseEntity<List<DentistServiceDto>> getAllDentistServices(){
        return ResponseEntity.ok(dentistServiceManager.getAllDentistServices());
    }


    @GetMapping("/getDentistServicesByDentistId/{dentistId}")
    public ResponseEntity<List<DentistServiceDto>> getDentistServicesByDentistId(@PathVariable Long dentistId){
        return ResponseEntity.ok(dentistServiceManager.getDentistServicesByDentistId(dentistId));
    }

    @PostMapping("/addDentistService")
    public ResponseEntity<DentistServiceDto> addDentistService(@RequestBody DentistServiceDto dentistServiceDto){
        return ResponseEntity.ok(dentistServiceManager.createDentistService(dentistServiceDto));
    }

    @PutMapping("/updateDentistService")
    public ResponseEntity<DentistServiceDto> updateDentistService(@RequestBody DentistServiceDto dentistServiceDto){
        return ResponseEntity.ok(dentistServiceManager.updateDentistService(dentistServiceDto));
    }

    @DeleteMapping("/deleteDentistService/{id}")
    public ResponseEntity<Void> deleteDentistService(@PathVariable Long id){
        dentistServiceManager.deleteDentistService(id);
        return ResponseEntity.ok().build();
    }
}
