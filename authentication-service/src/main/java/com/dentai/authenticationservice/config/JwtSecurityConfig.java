package com.dentai.authenticationservice.config;

import com.dentai.authenticationservice.infrastructure.JwtFilter;
import com.dentai.authenticationservice.infrastructure.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    Logger logger = org.slf4j.LoggerFactory.getLogger(JwtSecurityConfig.class);

    private final TokenProvider tokenProvider;

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        logger.info("JwtSecurityConfig: JwtFilter customFilter created.");
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        logger.info("JwtSecurityConfig: JwtFilter customFilter added to UsernamePasswordAuthenticationFilter.");
    }
    
}
