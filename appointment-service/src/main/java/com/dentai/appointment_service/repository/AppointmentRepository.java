package com.dentai.appointment_service.repository;

import com.dentai.appointment_service.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Modifying
    @Query("UPDATE Appointment a SET a.isDeleted = true, a.updatedBy = :updatedBy WHERE a.id = :id")
    void softDeleteById(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

}
