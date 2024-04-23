package com.co.solia.emotional.controllers.validators;


import com.co.solia.emotional.models.dtos.DefaultRsDto;
import com.co.solia.emotional.models.exceptions.InternalServerException;
import com.co.solia.emotional.models.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Stream;

/**
 * control advice for the response codes for the consumers.
 *
 * @author luis.bolivar
 */
@ControllerAdvice
@Slf4j
public class ControllerHandler {

    /**
     * {@link ExceptionHandler} for {@link InternalServerException}.
     * @param ise {@link InternalServerException} to catch.
     * @return {@link ExceptionHandler} for {@link DefaultRsDto}.
     */
    @ExceptionHandler(value = InternalServerException.class)
    public ResponseEntity<DefaultRsDto> internalException(final InternalServerException ise) {
        log.error("[internalException]: Error catching: message: {}, endpoint: {}", ise.getMessage(), ise.getEndpoint());
        logError(ise);
        return ResponseEntity.status(HttpStatusCode.valueOf(500))
                .body(DefaultRsDto.builder().message(ise.getMessage()).endpoint(ise.getEndpoint()).build());
    }

    /**
     * {@link ExceptionHandler} for {@link BadRequestException}.
     * @param bre {@link BadRequestException} to catch.
     * @return {@link ExceptionHandler} for {@link BadRequestException}.
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<DefaultRsDto> badRequestException(final BadRequestException bre) {
        log.error("[badRequestException]: Error catching: message: {}, endpoint: {}", bre.getMessage(), bre.getEndpoint());
        logError(bre);
        return ResponseEntity.status(HttpStatusCode.valueOf(404))
                .body(DefaultRsDto.builder().message(bre.getMessage()).endpoint(bre.getEndpoint()).build());
    }

    /**
     * get log for the runtime exception.
     * @param rte runtime exception.
     */
    private void logError(final RuntimeException rte){
        Stream.of(rte)
                .filter(e -> e.getCause() != null)
                .findFirst()
                .ifPresent(e -> log.error("[logError]: Error catching: cause: {}", e.getCause().getMessage()));
    }
}
