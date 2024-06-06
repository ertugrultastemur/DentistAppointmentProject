package com.dentai.usermanagementservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@Table(name = "medicines")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLRestriction("is_deleted = false")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "dosage")
    private Integer dosage;

    @Column(name = "period")
    private Integer period;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "use_count")
    private Integer useCount;

    @Column(name = "box_quantity")
    private Integer boxQuantity;

    @ManyToMany(mappedBy = "medicines",cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Prescription> prescriptions;


    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

}
