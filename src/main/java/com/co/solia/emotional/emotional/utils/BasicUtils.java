package com.co.solia.emotional.emotional.utils;

import com.co.solia.emotional.emotional.models.exceptions.InternalServerException;
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
            throw InternalServerException.builder().message("Error getting the variable").endpoint("SystemConfigs").build();
        }
        validateEnvVar(resultValue, envVariable);
        return resultValue;
    }

    /**
     * Validate the value of the environment variable it was got ok.
     * @param valEnvVar value to validate.
     * @param nameEnvVar name of the environment variable.
     * @throws InternalServerException if validation fails.
     */
    private static void validateEnvVar(final String valEnvVar, final String nameEnvVar){
        Stream.of(valEnvVar)
                .filter(BasicValidator::isValidString)
                .findFirst()
                .orElseGet(
                () -> {
                    log.error("[validateEnvVar]: Error getting the variable: {}", nameEnvVar);
                    throw InternalServerException.builder().message("Error getting the variable").endpoint("SystemConfigs").build();
                });
    }
}
