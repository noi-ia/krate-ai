package com.co.solia.emotional.emotional.models.dtos.rs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * class to map the result of multiple messages processed from emotional api.
 *
 * @author luis.bolivar
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class EmotionalBatchRsDto {

    /**
     * id of emotional estimation batch process.
     */
    @Schema(
            name = "id",
            description = "identifier of batch processing.",
            type = "UUID",
            example = "b6fbb344-da94-4433-b3f6-d67540d32ccc")
    private UUID id;

    /**
     * results from emotional processing.
     */
    @Schema(
            name = "results",
            description = "the results of batch processing.",
            type = "List")
    private List<EmotionalRsDto> results;
}
