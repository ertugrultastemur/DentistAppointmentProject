package com.dentai.authenticationservice.config;

import com.dentai.authenticationservice.exceptions.SecurityContextException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
public class SecurityUtil {


    public static String getCurrentMemberId() {
        Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityUtil.class);

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("SecurityContextHolder.getContext().getAuthentication(): {}", authentication);
        if (authentication == null || authentication.getName() == null) {
            logger.error("SecurityContextHolder.getContext().getAuthentication().getName() is null");
            throw new SecurityContextException("There's no authentication info in the security context");
        }
        logger.info("SecurityContextHolder.getContext().getAuthentication().getName(): {}", authentication.getName());
        return authentication.getName();
    }

}
