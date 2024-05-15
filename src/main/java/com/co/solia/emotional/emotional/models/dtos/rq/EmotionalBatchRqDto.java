package com.co.solia.emotional.emotional.models.dtos.rq;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * entity for multiple messages for emotional processing.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmotionalBatchRqDto {

    /**
     * the list of messages to estimate.
     */
    @Schema(
            name = "messages",
            description = "messages to generate the emotional estimation.",
            type = "List")
    private List<String> messages;
}
