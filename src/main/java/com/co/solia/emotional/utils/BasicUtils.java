package com.co.solia.emotional.utils;

import com.co.solia.emotional.models.exceptions.InternalServerException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

/**
 *  the general methods to use in couple places of app.
 *
 * @author luis.bolivar
 */
@UtilityClass
@Slf4j
public class BasicUtils {

    /**
     * get the value of an environment variable.
     * @param envVariable to get value.
     * @return {@link String}.
     * @throws InternalServerException in case that not found environment variable.
     */
    public static String getEnvVariable(final String envVariable){
        String resultValue;
        try {
            resultValue = System.getenv(envVariable);
        } catch (final Exception e){
            log.error("[getEnvVariable]: Error getting the variable: {}: error: {}", envVariable, e.getMessage());
            throw new InternalServerException("Error getting the variable", "SystemConfigs");
        }
        validateEnvVar(resultValue);
        return resultValue;
    }

    /**
     * Validate the value of the environment variable it was got ok.
     * @param envVariable to validate.
     * @throws InternalServerException if validation fails.
     */
    private static void validateEnvVar(final String envVariable){
        Stream.of(envVariable)
                .filter(BasicValidator::isValidString)
                .findFirst()
                .ifPresentOrElse(enVar -> log.info("[getEnvVariable]: getting successfully the variable")
                ,() -> {throw new InternalServerException("Error getting the variable", "SystemConfigs");});
    }
}
