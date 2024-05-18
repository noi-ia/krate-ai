package com.co.solia.emotional.emotional.utils;

import lombok.experimental.UtilityClass;

import java.time.Instant;

/**
 * basic validations on fields.
 *
 * @author luis.bolivar.
 */
@UtilityClass
public class BasicUtils {

    /**
     * validate the string field.
     * @param value to validate
     * @return {@link Boolean}.
     */
    public static Boolean isValidString(final String value){
        return value != null && !value.isEmpty() && !value.isBlank();
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
     * get the milliseconds to start a process.
     * @return a long duration.
     */
    public static long getStart() {
        return Instant.now().toEpochMilli();
    }
}
