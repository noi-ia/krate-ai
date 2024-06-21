package com.co.solia.emotional.share.models.validators;

import com.co.solia.emotional.share.models.exceptions.BadRequestException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * basic validations on fields.
 *
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class BasicValidator {

    /**
     * validate the string field.
     * @param value to validate
     * @return {@link Boolean}.
     */
    public static Boolean isValidString(final String value){
        return value != null && !value.isEmpty() && !value.isBlank();
    }

    /**
     * validate if an id is valid.
     * @param id to be validated.
     * @return {@link Boolean}.
     * */
    public static Boolean isValidId(final UUID id) {
        return id != null;
    }

    /**
     * get duration of openai processing.
     * @param start date.
     * @param end date.
     * @return {@link Long} total duration of openai processing.
     */
    public static long getDuration(long start, long end) {
        return end - start;
    }

    /**
     * get duration of openai processing.
     * @param start date.
     * @return {@link Long} total duration of openai processing.
     */
    public static long getDuration(long start) {
        return getNow() - start;
    }

    /**
     * get the milliseconds to start a process.
     * @return a long duration.
     */
    public static long getNow() {
        return Instant.now().toEpochMilli();
    }

    /**
     * validate if a field is valid or invalid
     * @param isValid evaluation of the field
     * @param field to be validated.
     */
    public void isValidField(final Boolean isValid, final String field) {
        Stream.of(isValid)
                .filter(Boolean.FALSE::equals)
                .findFirst()
                .map(valid -> {
                    log.error("[isValidField] the field {} is invalid, please check it out.", field);
                    throw BadRequestException.builder()
                            .endpoint("/campaign/compute")
                            .message("the field " + field +  " is invalid, please check it out")
                            .build();
                });
    }
}
