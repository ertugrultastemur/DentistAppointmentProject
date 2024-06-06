package com.dentai.usermanagementservice.service;

import com.dentai.usermanagementservice.exception.InvalidUserDataException;
import com.dentai.usermanagementservice.exception.XRayNotFoundException;
import com.dentai.usermanagementservice.model.XRay;
import com.dentai.usermanagementservice.repository.PatientRepository;
import com.dentai.usermanagementservice.dto.XRayDto;
import com.dentai.usermanagementservice.exception.UserNotFoundException;
import com.dentai.usermanagementservice.model.Patient;
import com.dentai.usermanagementservice.repository.XRayRepository;
import com.dentai.usermanagementservice.util.HeaderUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class XRayService {

    private final XRayRepository xRayRepository;

    private final PatientRepository patientRepository;

    @Value("${detection-service-url}")
    private String url;

    public XRayService(XRayRepository xRayRepository, PatientRepository patientRepository) {
        this.xRayRepository = xRayRepository;
        this.patientRepository = patientRepository;
    }

    public List<XRayDto> getAllXRaysByPatientId(Long patientId) {
        List<XRay> xrays = xRayRepository.findAllByPatientId(patientId).orElseThrow(() -> new UserNotFoundException("XRays not found by patient id: " + patientId));
        return xrays.stream().map(XRayDto::convert).toList();
    }

    public List<XRayDto> getAllXRays() {
        return xRayRepository.findAll().stream().map(XRayDto::convert).collect(Collectors.toList());
    }

    public XRayDto getXRayById(Long id) {
        return XRayDto.convert(xRayRepository.findById(id).orElseThrow(() -> new XRayNotFoundException(id)));
    }

    @Transactional
    public void addXRay(XRayDto xRayDto, List<MultipartFile> images, HttpServletRequest request) throws IOException, SQLException {
        Patient patient = patientRepository.findById(xRayDto.getPatientId()).orElseThrow(() -> new XRayNotFoundException(xRayDto.getPatientId()));
        for (MultipartFile imageIndex : images) {
            byte[] imageBytes = imageIndex.getBytes();
            Blob imageBlob = new SerialBlob(imageBytes);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            String jsonBody = "{ \"image\": \"" + base64Image + "\" }";

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody.getBytes(StandardCharsets.UTF_8));
            Request request1 = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = client.newCall(request1).execute();

            if (!response.isSuccessful() || response.body() == null) {
                throw new InvalidUserDataException("Failed to upload image: " + response.message());
            }

            String responseString = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(responseString);

            String imageBase64 = responseJson.get("image").asText();
            String xrayBase64 = responseJson.get("xray").asText();

            byte[] newImageBytes = Base64.getDecoder().decode(imageBase64);
            byte[] xrayBytes = Base64.getDecoder().decode(xrayBase64);

            Blob processedImageBlob = new SerialBlob(newImageBytes);
            Blob processedXrayBlob = new SerialBlob(xrayBytes);

            XRay xRay = XRay.builder()
                    .image(processedImageBlob)
                    .patient(patient)
                    .createdBy(HeaderUtil.getUserId(request))
                    .build();

            XRay processedXRay = XRay.builder()
                    .image(processedXrayBlob)
                    .patient(patient)
                    .createdBy(HeaderUtil.getUserId(request))
                    .build();

            xRayRepository.saveAll(List.of(xRay, processedXRay));
        }
    }

    public void updateXRay(MultipartFile image, XRayDto xRayDto, HttpServletRequest request) throws IOException, SQLException {
        Patient patient = patientRepository.findById(xRayDto.getPatientId()).orElseThrow(() -> new XRayNotFoundException(xRayDto.getPatientId()));
        XRay xRay = xRayRepository.findById(xRayDto.getId()).orElseThrow(() -> new XRayNotFoundException(xRayDto.getId()));
        byte[] imageBytes = image.getBytes();
        Blob imageBlob = new SerialBlob(imageBytes);
        xRay.setImage(imageBlob);
        xRay.setUpdatedBy(HeaderUtil.getUserId(request));
        xRay.setPatient(patient);
        xRayRepository.save(xRay);
    }

    public void deleteXRay(Long id, HttpServletRequest request) {
        if (!xRayRepository.existsById(id)) {
            throw new XRayNotFoundException(id);
        }
        xRayRepository.softDeleteById(id, HeaderUtil.getUserId(request));
    }


}
