package com.dentai.authenticationservice.service;


import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class MemberService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(MemberService.class);
    private final PasswordEncoder passwordEncoder;

    public MemberService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public String passwordEncode(String password){
        logger.trace("MemberService: passwordEncode called");
        logger.info("MemberService: passwordEncode started.");
        return passwordEncoder.encode(password);
    }
}
