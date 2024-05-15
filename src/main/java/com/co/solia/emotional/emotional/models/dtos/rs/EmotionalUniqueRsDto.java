package com.co.solia.emotional.emotional.models.dtos.rs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * the response to the unique emotional compute in messages.
 *
 * @author luis.bolivar
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class EmotionalUniqueRsDto {

    /**
     * the unique emotional compute id.
     */
    @Schema(
            name = "id",
            description = "identifier of emotional unique processing.",
            type = "UUID",
            example = "b6fbb344-da94-4433-b3f6-d67540d32ccc")
    private UUID id;

    /**
     * messages processed.
     */
    @Schema(
            name = "messages",
            description = "message to process with unique emotional estimation.",
            type = "List")
    private List<String> messages;

    /**
     * emotions results from estimation.
     */
    @Schema(
            name = "emotions",
            description = "emotions associated a the list of messages.",
            type = "Map")
    private Map<String, Double> emotions;
}
