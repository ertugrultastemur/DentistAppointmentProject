package com.dentai.authenticationservice.dto;


import com.dentai.authenticationservice.domain.Member;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Data
public class SignUpRequest {

    Logger logger = org.slf4j.LoggerFactory.getLogger(SignUpRequest.class);
    
    @Email(message = "Not a valid email")
    private String email;

    private String password;

    private Set<String> roles;

    public Member toMember(PasswordEncoder passwordEncoder) {
        logger.info("SignUpRequest: toMember started with email | {} and password | {}", this.email, this.password);
        return new Member(email, passwordEncoder.encode(password),roles);
    }
    
}
