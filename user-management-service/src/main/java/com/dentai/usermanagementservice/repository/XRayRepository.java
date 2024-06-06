package com.dentai.usermanagementservice.repository;

import com.dentai.usermanagementservice.model.XRay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface XRayRepository extends JpaRepository<XRay, Long> {

    Optional<List<XRay>> findAllByPatientId(Long id);

    @Modifying
    @Query(value = "UPDATE x_ray_images SET is_deleted = true, updated_by = :updatedBy WHERE id = :id", nativeQuery = true)
    void softDeleteById(@Param("id") Long id, @Param("updatedBy") Long updatedBy);
}
