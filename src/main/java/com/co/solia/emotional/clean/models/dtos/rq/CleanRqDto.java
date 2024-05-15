package com.co.solia.emotional.clean.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * class to map the request for cleaning messages.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CleanRqDto {

    /**
     * message to clean.
     */
    private String message;
}
