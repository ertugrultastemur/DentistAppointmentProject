package com.dentai.authenticationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    Logger logger = org.slf4j.LoggerFactory.getLogger(SignInRequest.class);

    @Email(message = "Not a valid email")
    private String email;
    
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        logger.info("SignInRequest: toAuthentication started with email | {} and password | {}", this.email, this.password);
        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }
    
}
