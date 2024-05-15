package com.co.solia.emotional.emotional.models.dtos.rq;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(
            name = "message",
            description = "message to process with emotional estimation.",
            type = "String",
            example = "I'm feeling so happy today.")
    private String message;
}
