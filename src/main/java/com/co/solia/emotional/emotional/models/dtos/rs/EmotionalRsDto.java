package com.co.solia.emotional.emotional.models.dtos.rs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * response body of emotional estimation.
 *
 * @author luis.bolivar
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmotionalRsDto {

    /**
     * emotional estimation id.
     */
    @Schema(
            name = "id",
            description = "identifier of emotional processing.",
            type = "UUID",
            example = "b6fbb344-da94-4433-b3f6-d67540d32ccc")
    private UUID id;

    /**
     * message processed.
     */
    @Schema(
            name = "message",
            description = "message to estimate the emotional rates.",
            type = "String",
            example = "I'm feeling so happy today.")
    private String message;

    /**
     * emotions related to estimation.
     */
    @Schema(
            name = "emotions",
            description = "emotions estimate from messages.",
            type = "Map")
    private Map<String, Double> emotions;
}
