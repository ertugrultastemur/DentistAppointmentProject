package com.dentai.usermanagementservice.repository;

import com.dentai.usermanagementservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<List<Patient>> findAllByUserId(Long userId);

    @Modifying
    @Query("UPDATE Patient p SET p.isDeleted = true, p.updatedBy = :updatedBy WHERE p.id = :id")
    void softDeleteById(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    Optional<Patient> findByEmail(String email);
}
