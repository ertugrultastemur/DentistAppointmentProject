package com.dentai.authenticationservice.config;

import com.dentai.authenticationservice.exceptions.JwtAccessDeniedHandler;
import com.dentai.authenticationservice.exceptions.JwtAuthenticationEntryPoint;
import com.dentai.authenticationservice.infrastructure.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfigure {

    Logger logger = org.slf4j.LoggerFactory.getLogger(WebSecurityConfigure.class);
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Password encoder created.");
        return new BCryptPasswordEncoder();
    }
    


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(eh-> eh.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler))
            .headers(header-> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .sessionManagement(sManagement-> sManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(requests -> requests
                    .requestMatchers("/", "/home")
                    .authenticated().anyRequest().permitAll())
            .apply(new JwtSecurityConfig(tokenProvider));
        logger.info("WebSecurityConfigure: filterChain created.");
        return http.build();
    }



}
