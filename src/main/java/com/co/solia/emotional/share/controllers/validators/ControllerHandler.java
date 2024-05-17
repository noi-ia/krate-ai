package com.co.solia.emotional.share.controllers.validators;


import com.co.solia.emotional.share.models.dtos.rs.DefaultRsDto;
import com.co.solia.emotional.share.models.exceptions.BadRequestException;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.models.exceptions.NotFoundException;
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
    @ExceptionHandler(value = {InternalServerException.class, Exception.class})
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
        return ResponseEntity.status(HttpStatusCode.valueOf(400))
                .body(DefaultRsDto.builder().message(bre.getMessage()).endpoint(bre.getEndpoint()).build());
    }

    /**
     * {@link ExceptionHandler} for {@link NotFoundException}.
     * @param nfe {@link NotFoundException} to catch.
     * @return {@link ExceptionHandler} for {@link NotFoundException}.
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<DefaultRsDto> NotFoundException(final NotFoundException nfe) {
        log.error("[NotFoundException]: Error catching: message: {}, endpoint: {}", nfe.getMessage(), nfe.getEndpoint());
        logError(nfe);
        return ResponseEntity.status(HttpStatusCode.valueOf(404))
                .body(DefaultRsDto.builder().message(nfe.getMessage()).endpoint(nfe.getEndpoint()).build());
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
