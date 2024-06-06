package com.dentai.usermanagementservice.repository;

import com.dentai.usermanagementservice.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.isDeleted = true, u.updatedBy = :updatedBy WHERE u.id = :id")
    void softDeleteById(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    @Query("SELECT u.name FROM User u WHERE u.isDeleted = false")
    Optional<List<String>> findAllNames();



}
