package com.dentai.authenticationservice.infrastructure;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    Logger logger = org.slf4j.LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = resolveToken(request);
        logger.info("JwtFilter: doFilterInternal started with jwt | {}", jwt);

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)!=null) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            logger.info("JwtFilter: doFilterInternal started with authentication | {}", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("JwtFilter: doFilterInternal started with security context | {}", SecurityContextHolder.getContext().getAuthentication());
        }

        filterChain.doFilter(request, response);
        logger.info("JwtFilter: doFilterInternal finished.");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        logger.info("JwtFilter: resolveToken started with bearer token | {}", bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            logger.info("JwtFilter: Substring | {}", bearerToken.substring(7));
            return bearerToken.substring(7);
        }
        logger.info("JwtFilter: return null");
        return null;
    }
    
}
