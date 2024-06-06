package com.dentai.appointment_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "services")
@SQLRestriction("is_deleted = false")
public class DentistService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration_minute")
    private Integer durationMinute;

    @ManyToMany(mappedBy = "dentistServices",cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Appointment> appointments;


    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
