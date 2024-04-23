package com.co.solia.emotional.emotional.models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Internal server exception.
 *
 * @author luis.bolivar
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class InternalServerException extends RuntimeException{
    /**
     * message about the exception.
     */
    private String message;

    /**
     * endpoint where was thrown the exception.
     */
    private String endpoint;

    /**
     * cause of the exception.
     */
    private Throwable cause;
}
