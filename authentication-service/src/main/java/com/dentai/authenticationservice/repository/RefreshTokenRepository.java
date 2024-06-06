package com.dentai.authenticationservice.repository;

import com.dentai.authenticationservice.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByKey (String key);

    boolean existsByValue (String value);

}
