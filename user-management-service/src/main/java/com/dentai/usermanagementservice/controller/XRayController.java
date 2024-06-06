package com.dentai.usermanagementservice.controller;

import com.dentai.usermanagementservice.dto.XRayDto;
import com.dentai.usermanagementservice.service.XRayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/user/xrays")
public class XRayController {

    private final XRayService xRayService;

    public XRayController(XRayService xRayService){
        this.xRayService = xRayService;
    }

    @GetMapping("/getAllXRays")
    public ResponseEntity<List<XRayDto>> getAllXRays(){
        return ResponseEntity.ok(xRayService.getAllXRays());
    }

    @GetMapping("/getAllXRaysByPatientId/{patientId}")
    public ResponseEntity<List<XRayDto>> getAllXRaysByPatientId(@PathVariable Long patientId){
        return ResponseEntity.ok(xRayService.getAllXRaysByPatientId(patientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<XRayDto> getXRayById(@PathVariable Long id){
        return ResponseEntity.ok(xRayService.getXRayById(id));
    }

    @PostMapping(value = "/addXRay", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addXRay(@RequestParam("images") List<MultipartFile> images, @ModelAttribute @RequestPart("xRay") XRayDto xRay, HttpServletRequest request) throws SQLException, IOException {
        xRayService.addXRay(xRay, images, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateXRay")
    public ResponseEntity<XRayDto> updateXRay(@RequestParam() MultipartFile image, @ModelAttribute @RequestPart("xRay") XRayDto xRay, HttpServletRequest request) throws SQLException, IOException {
        xRayService.updateXRay(image, xRay, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteXRay/{id}")
    public ResponseEntity<Void> deleteXRay(@PathVariable Long id, HttpServletRequest request){
        xRayService.deleteXRay(id, request);
        return ResponseEntity.ok().build();
    }

}
