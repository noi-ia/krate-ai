package com.co.solia.emotional.utils;

import lombok.experimental.UtilityClass;

/**
 * basic validations on fields.
 *
 * @author luis.bolivar.
 */
@UtilityClass
public class BasicValidator {

    /**
     * validate the string field.
     * @param value to validate
     * @return {@link Boolean}.
     */
    public static Boolean isValidString(final String value){
        return value != null && !value.isEmpty() && !value.isBlank();
    }
}
