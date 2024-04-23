package com.co.solia.emotional.models.dtos;

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
public class EmotionalMessageRqDto {

    /**
     * message to be processed.
     */
    private String message;
}
