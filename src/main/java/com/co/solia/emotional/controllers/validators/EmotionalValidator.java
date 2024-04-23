package com.co.solia.emotional.controllers.validators;


import com.co.solia.emotional.models.exceptions.BadRequestException;
import com.co.solia.emotional.utils.BasicValidator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

/**
 * utility class to validate the request and response in emotional calls.
 *
 * @author luis.bolivar
 */
@UtilityClass
@Slf4j
public class EmotionalValidator {

    /**
     * method to validate the message to be processed by emotional estimation.
     * @param message to validate.
     */
    public static void validateMessage(final String message) {
        final Boolean isValidMessage = BasicValidator.isValidString(message);
        Stream.of(isValidMessage)
                .filter(Boolean.TRUE::equals)
                .findFirst()
                .ifPresentOrElse(validMessage ->
                    log.info("[validateMessage]: message is valid to process.")
                    , () -> {
                            log.error("[validateMessage]: the message to process is invalid.");
                            throw BadRequestException.builder()
                                    .message("the message to process is invalid.")
                                    .endpoint("/emotional/")
                                    .build();
                        });
    }
}
