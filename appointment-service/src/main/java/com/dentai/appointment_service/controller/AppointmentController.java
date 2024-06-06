package com.dentai.appointment_service.controller;

import com.dentai.appointment_service.dto.AppointmentDto;
import com.dentai.appointment_service.dto.UpdateAppointmentDto;
import com.dentai.appointment_service.service.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id){
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/getAllAppointments")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments(){
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PostMapping("/addAppointment")
    public ResponseEntity<Void> addAppointment(@RequestBody UpdateAppointmentDto appointmentDto, HttpServletRequest request){
        appointmentService.createAppointment(appointmentDto, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateAppointment")
    public ResponseEntity<AppointmentDto> updateAppointment(@RequestBody UpdateAppointmentDto appointmentDto, HttpServletRequest request){
        return ResponseEntity.ok(appointmentService.updateAppointment(appointmentDto, request));
    }

    @DeleteMapping("/deleteAppointment/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id, HttpServletRequest request){
        appointmentService.deleteAppointment(id, request);
        return ResponseEntity.ok().build();
    }
}
