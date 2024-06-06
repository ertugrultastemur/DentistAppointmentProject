package com.dentai.authenticationservice.controller;

import com.dentai.authenticationservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth/member")
@RequiredArgsConstructor
public class MemberController {
    Logger logger = org.slf4j.LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;


    @PostMapping("/passwordEncode/{password}")
    public String passwordEncode(@PathVariable String password){
        logger.trace("MemberController: passwordEncode called");
        logger.info("MemberController: passwordEncode started with password | {}", password);
        return memberService.passwordEncode(password);
    }

}
