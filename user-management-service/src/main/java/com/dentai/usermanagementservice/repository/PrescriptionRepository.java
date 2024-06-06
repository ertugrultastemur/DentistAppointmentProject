package com.dentai.usermanagementservice.repository;

import com.dentai.usermanagementservice.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Optional<List<Prescription>> findByPatientId(Long patientId);

    @Modifying
    @Query("UPDATE Prescription u SET u.isDeleted = true, u.updatedBy = :updatedBy WHERE u.id = :id")
    void softDeleteById(@Param("id") Long id, @Param("updatedBy") Long updatedBy);
}
