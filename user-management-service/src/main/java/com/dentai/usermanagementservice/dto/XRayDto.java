package com.dentai.usermanagementservice.dto;

import com.dentai.usermanagementservice.model.XRay;
import lombok.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class XRayDto {

    private Long id;

    private String imageBase64;

    private Long patientId;

    public static XRayDto convert(XRay xRay) {
        return XRayDto.builder()
                .id(xRay.getId())
                .imageBase64(convertBlobToBase64(xRay.getImage()))
                .patientId(xRay.getPatient().getId())
                .build();
    }

    public static String convertBlobToBase64(Blob blob) {
        try (InputStream inputStream = blob.getBinaryStream()) {
            int blobLength = (int) blob.length();
            byte[] bytes = new byte[blobLength];
            inputStream.read(bytes, 0, blobLength);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
