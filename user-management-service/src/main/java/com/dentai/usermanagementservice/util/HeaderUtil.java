package com.dentai.usermanagementservice.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HeaderUtil {

    static Logger logger = LoggerFactory.getLogger(HeaderUtil.class);

    static String USER_ID = "X-USER-ID";



    public static Long getUserId(HttpServletRequest request) {
        logger.trace("HeaderUtil: getUserId started");
        if (request.getHeader(USER_ID) == null) {
            logger.warn("HeaderUtil: getUserId | Header not found: {}", USER_ID);
            return 999L;
        }
        logger.info("HeaderUtil: getUserId finished | {}", request.getHeader(USER_ID));
        return request.getHeader(USER_ID).transform(Long::parseLong);
    }
}
