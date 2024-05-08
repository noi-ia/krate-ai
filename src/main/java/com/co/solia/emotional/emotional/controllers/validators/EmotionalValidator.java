package com.co.solia.emotional.emotional.controllers.validators;


import com.co.solia.emotional.emotional.models.exceptions.BadRequestException;
import com.co.solia.emotional.emotional.utils.BasicValidator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
        Stream.of(message)
                .filter(BasicValidator::isValidString)
                .findFirst()
                .ifPresentOrElse(validMessage ->
                    log.info("[validateMessage]: message is valid to process.")
                    , () -> {
                            log.error("[validateMessage]: the message to process is invalid.");
                            throw BadRequestException.builder()
                                    .message("the message to process is invalid.")
                                    .endpoint("/emotional/compute/")
                                    .build();
                        });
    }

    /**
     * validate the id of emotional estimation.
     * @param idEE estimation id.
     */
    public static void validateIdEe(final UUID idEE){
        Stream.of(idEE)
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresentOrElse(id -> log.info("[validateIdEe]: id of emotional estimation format is ok")
                        , () -> {
                            log.error("[validateIdEe]: the id of emotional estimation is invalid.");
                            throw BadRequestException.builder()
                                    .message("the id to process is invalid.")
                                    .endpoint("/emotional/{id}")
                                    .build();
                        });
    }

    /**
     * validate the messages to compute.
     * @param messages to compute.
     */
    public static void validateMessages(final List<String> messages) {
        Stream.of(messages)
                .filter(Objects::nonNull)
                .filter(m -> !m.isEmpty())
                .filter(m -> m.getFirst() != null)
                .filter(m -> !m.getFirst().isEmpty())
                .filter(m -> !m.getFirst().isBlank())
                .findFirst()
                .ifPresentOrElse(m -> log.info("[validateMessages]: messages to compute are ok"),
                        () -> {
                            log.error("[validateMessages]: Error in messages, validate them.");
                            throw BadRequestException.builder()
                                    .message("the message to compute are invalid.")
                                    .endpoint("/emotional/compute/")
                                    .build();
                        });
    }
}
