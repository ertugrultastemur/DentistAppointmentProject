package com.dentai.usermanagementservice.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.sql.Blob;
import java.time.LocalDateTime;

@Entity
@Table(name = "x_ray_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE x_ray_images SET is_deleted = true WHERE id = :id")
@SQLRestriction("is_deleted = false")
public class XRay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Blob image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

}
