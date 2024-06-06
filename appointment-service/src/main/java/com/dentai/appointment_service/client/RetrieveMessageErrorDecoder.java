package com.dentai.appointment_service.client;


import com.dentai.appointment_service.exception.ExceptionMessage;
import com.dentai.authenticationservice.exceptions.UserNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RetrieveMessageErrorDecoder implements ErrorDecoder {

    final ErrorDecoder errorDecoder = new Default();

    Logger log = org.slf4j.LoggerFactory.getLogger(RetrieveMessageErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        log.debug("RetrieveMessageErrorDecoder: decode method entered.");
        ExceptionMessage message = null;
        try (InputStream body = response.body().asInputStream()) {
            message = new ExceptionMessage((String) response.headers().get("date").toArray()[0],
                    response.status(),
                    HttpStatus.resolve(response.status()).getReasonPhrase(),
                    IOUtils.toString(body, StandardCharsets.UTF_8),
                    response.request().url());
            log.debug("RetrieveMessageErrorDecoder: message created.");

        } catch (IOException exception) {
            log.error("RetrieveMessageErrorDecoder: message creating failed. {}", exception.getMessage());
            return new Exception(exception.getMessage());
        }
        return switch (response.status()) {
            case 404 -> {
                log.error("RetrieveMessageErrorDecoder: 500 error: {}", message);
                throw new UserNotFoundException(message);
            }
            default -> {
                log.error("RetrieveMessageErrorDecoder: error: {}", message);
                yield errorDecoder.decode(methodKey, response);
            }
        };
    }
}
