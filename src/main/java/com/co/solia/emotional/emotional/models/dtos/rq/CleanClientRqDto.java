package com.co.solia.emotional.emotional.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * class to map the request for cleaning message.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CleanClientRqDto {

    /**
     * message to clean.
     */
    private String message;

}
