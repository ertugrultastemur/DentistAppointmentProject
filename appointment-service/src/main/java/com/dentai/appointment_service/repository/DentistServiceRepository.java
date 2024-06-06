package com.dentai.appointment_service.repository;

import com.dentai.appointment_service.model.Appointment;
import com.dentai.appointment_service.model.DentistService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DentistServiceRepository extends JpaRepository<DentistService, Long> {

    @Modifying
    @Query("UPDATE DentistService d SET d.isDeleted = true WHERE d.id = :id")
    void softDeleteById(@Param("id") Long id);

    Optional<DentistService> findByName(String name);

}
