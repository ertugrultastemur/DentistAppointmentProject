package com.dentai.authenticationservice.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    Logger logger = org.slf4j.LoggerFactory.getLogger(JwtAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.error("JwtAccessDeniedHandler: handle started with exception | {}", accessDeniedException.getMessage());
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        logger.error("JwtAccessDeniedHandler: sendError with status code | {}", HttpServletResponse.SC_FORBIDDEN);
    }
    
}
