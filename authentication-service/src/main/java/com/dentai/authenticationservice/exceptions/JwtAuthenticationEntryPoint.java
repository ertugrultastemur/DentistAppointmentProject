package com.dentai.authenticationservice.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    Logger logger = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logger.error("JwtAuthenticationEntryPoint: commence started with exception | {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        logger.error("JwtAuthenticationEntryPoint: sendError with status code | {}", HttpServletResponse.SC_UNAUTHORIZED);
    }

}
