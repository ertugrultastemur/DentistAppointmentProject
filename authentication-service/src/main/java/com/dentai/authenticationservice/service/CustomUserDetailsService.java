package com.dentai.authenticationservice.service;

import com.dentai.authenticationservice.client.UserManagementServiceClient;
import com.dentai.authenticationservice.domain.Member;
import com.dentai.authenticationservice.dto.UserDto;
import com.dentai.authenticationservice.mapper.ModelMapperService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserManagementServiceClient userManagementServiceClient;

    private final ModelMapperService modelMapperService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.trace("CustomUserDetailsService: loadUserByUsername started.");
        UserDto user = userManagementServiceClient.getUserByEmail(username).getBody();
        logger.info("CustomUserDetailsService: user has been collected.");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info("CustomUserDetailsService: password has been encoded.");
        Optional<Member> member = Optional.ofNullable(modelMapperService.forRequest().map(user, Member.class));
        logger.info("CustomUserDetailsService: member has been created.");
        logger.trace("CustomUserDetailsService: loadUserByUsername finished.");
        return member
                .map(this::createUserDetails)
                .orElseThrow(() -> {
                    logger.error("CustomUserDetailsService: loadUserByUsername failed. User not found. | {}", username);
                    return new UsernameNotFoundException(username + " -> There's no such User.");});
    }

    private UserDetails createUserDetails(Member member) {
        logger.trace("CustomUserDetailsService: createUserDetails started.");
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        logger.info("CustomUserDetailsService: grantedAuthority has been created.");
        logger.trace("CustomUserDetailsService: createUserDetails finished.");
        return new User(
                member.getEmail(),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
    
}
