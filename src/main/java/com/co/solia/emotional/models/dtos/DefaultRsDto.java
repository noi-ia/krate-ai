package com.co.solia.emotional.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * default dto to response in exceptions or other cases.
 *
 * @author luis.bolivar
 */

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultRsDto {

    /**
     * message to send to the client.
     */
    private String message;

    /**
     * endpoint related to the client.
     */
    private String endpoint;
}
