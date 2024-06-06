package com.dentai.usermanagementservice.repository;

import com.dentai.usermanagementservice.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Optional<Medicine> findByName(String name);

/*    @Query("select m from Medicine m where m.prescriptions = :prescriptionId and m.isDeleted = false")
    Optional<List<Medicine>> findMedicinesByPrescriptionId(@Param("prescriptionId") Long prescriptionId);*/

    @Modifying
    @Query("update Medicine m set m.isDeleted = true where m.id = :id")
    void softDeleteById(@Param("id") Long id);
}
