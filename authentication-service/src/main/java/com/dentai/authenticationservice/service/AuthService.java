package com.dentai.authenticationservice.service;

import com.dentai.authenticationservice.client.UserManagementServiceClient;
import com.dentai.authenticationservice.domain.RefreshToken;
import com.dentai.authenticationservice.domain.Role;
import com.dentai.authenticationservice.dto.SignInRequest;
import com.dentai.authenticationservice.dto.TokenRequest;
import com.dentai.authenticationservice.dto.UserDto;
import com.dentai.authenticationservice.dto.ValidationResponseDto;
import com.dentai.authenticationservice.exceptions.TokenNotValidException;
import com.dentai.authenticationservice.exceptions.UserNotFoundException;
import com.dentai.authenticationservice.infrastructure.TokenProvider;
import com.dentai.authenticationservice.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserManagementServiceClient userManagementServiceClient;



    @Transactional
    public TokenRequest signIn(SignInRequest signinRequest) {
        logger.trace("AuthService: signIn started.");

        UserDto userDto = userManagementServiceClient.getUserByEmail(signinRequest.getEmail()).getBody();
        logger.info("AuthService signIn: userDto has been collected.");
        if (!passwordEncoder.matches(signinRequest.getPassword(), userDto.getPassword())) {
            logger.error("AuthService: signIn failed. Invalid Password. | {}", signinRequest.getPassword());
            throw new InvalidParameterException("Invalid Password.");
        }
        signinRequest.setPassword(userDto.getPassword());
        logger.info("AuthService signIn: password has been checked.");

        UsernamePasswordAuthenticationToken authenticationToken = signinRequest.toAuthentication();
        logger.info("AuthService signIn: authenticationToken has been created.");

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        logger.info("AuthService signIn: authentication has been authenticated.");

        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        logger.info("AuthService signIn: authorities has been created.");

        for (Role role : userDto.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
            logger.info("AuthService signIn: role has been added.");
        }

        Authentication authenticatedUser = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
        logger.info("AuthService signIn: authenticatedUser has been created.");

        TokenRequest tokenRequest = tokenProvider.generateTokenDto(authenticatedUser);
        logger.info("AuthService signIn: tokenRequest has been created.");

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenRequest.getRefreshToken())
                .build();
        logger.info("AuthService signIn: refreshToken has been created.");
        refreshTokenRepository.save(refreshToken);
        logger.info("AuthService signIn: refreshToken has been saved.");
        logger.trace("AuthService: signIn finished.");
        return tokenRequest;
    }

    @Transactional
    public ValidationResponseDto validateToken(String token) {
        logger.trace("AuthService: validateToken started.");
        logger.info("AuthService validateToken: token has been checked.");
        return tokenProvider.validateToken(token);
    }

    @Transactional
    public TokenRequest refresh(TokenRequest tokenRequest) {
        logger.trace("AuthService: refresh started.");
        if (!refreshTokenRepository.existsByValue(tokenRequest.getRefreshToken())) {
            throw new TokenNotValidException("Session expired.");
        }
        ValidationResponseDto validationResponse = tokenProvider.validateToken(tokenRequest.getAccessToken());

        if (validationResponse != null && validationResponse.isValid()) {
            return tokenRequest;
        }
        UserDto userDto = userManagementServiceClient.getUserById(tokenProvider.validateRefreshToken(tokenRequest.getRefreshToken()).getId()).getBody();
        Authentication newAuthentication = authenticationManagerBuilder.getObject().authenticate(tokenProvider.refreshTokenAuthentication(tokenRequest, userDto));
        RefreshToken refreshToken = refreshTokenRepository.findByKey(newAuthentication.getName())
                .orElseThrow(() -> {
                    logger.error("AuthService: refresh failed. This member is signed out. | {}", newAuthentication.getName());
                    return new TokenNotValidException("This member is signed out.");});
        List<GrantedAuthority> authorities = new ArrayList<>(newAuthentication.getAuthorities());
        logger.info("AuthService signIn: authorities has been created.");

        for (Role role : userDto.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
            logger.info("AuthService signIn: role has been added.");
        }

        Authentication authenticatedUser = new UsernamePasswordAuthenticationToken(newAuthentication.getPrincipal(), newAuthentication.getCredentials(), authorities);
        logger.info("AuthService signIn: authenticatedUser has been created.");
        TokenRequest newTokenRequest = tokenProvider.generateRefreshTokenDto(authenticatedUser, refreshToken.getValue());
        logger.info("AuthService refresh: newTokenRequest has been created.");

        RefreshToken newRefreshToken = refreshToken.updateValue(newTokenRequest.getRefreshToken());
        logger.info("AuthService refresh: newRefreshToken has been created and updated.");
        refreshTokenRepository.save(newRefreshToken);
        logger.info("AuthService refresh: newRefreshToken has been saved.");
        logger.trace("AuthService: refresh finished.");
        return newTokenRequest;
    }
    
}
