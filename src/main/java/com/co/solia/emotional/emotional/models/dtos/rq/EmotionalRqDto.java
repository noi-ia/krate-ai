package com.co.solia.emotional.emotional.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * request to process a message.
 *
 * @author luis.bolivar
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmotionalRqDto {

    /**
     * message to be processed.
     */
    private String message;
}
