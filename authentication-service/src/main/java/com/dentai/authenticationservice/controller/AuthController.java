package com.dentai.authenticationservice.controller;

import com.dentai.authenticationservice.dto.SignInRequest;
import com.dentai.authenticationservice.dto.TokenRequest;
import com.dentai.authenticationservice.dto.ValidationResponseDto;
import com.dentai.authenticationservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth/auth")
@RequiredArgsConstructor
public class AuthController {

    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<TokenRequest> login(@RequestBody @Valid final SignInRequest signInRequest) {
        logger.trace("AuthController: login called");
        logger.info("AuthController: login started with signInRequest | {}", signInRequest);
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }


    @GetMapping("/validate")
    public ResponseEntity<ValidationResponseDto> validate(@RequestParam("token") String token){
        logger.trace("AuthController: validate called");
        logger.info("AuthController: validate started with token | {}", token);
        return ResponseEntity.ok(authService.validateToken(token));
    }


    @PostMapping("/refresh")
    public ResponseEntity<TokenRequest> refresh(@RequestBody TokenRequest tokenRequestDto) {
        logger.trace("AuthController: refresh called");
        logger.info("AuthController: refresh started with tokenRequestDto | {}", tokenRequestDto);
        return ResponseEntity.ok(authService.refresh(tokenRequestDto));
    }
}
