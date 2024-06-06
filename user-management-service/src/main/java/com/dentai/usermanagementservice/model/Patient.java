package com.dentai.usermanagementservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "patients")
@Entity
@SQLRestriction("is_deleted = false")
public class Patient {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PatientStatus status;

    @Column(name = "completion_percentage")
    private Integer completionPercentage;

    @Column(name = "performed_operations")
    @ElementCollection
    private List<String> performedOperations;

    @Column(name = "age")
    private Integer age;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<XRay> xRays;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;
}
