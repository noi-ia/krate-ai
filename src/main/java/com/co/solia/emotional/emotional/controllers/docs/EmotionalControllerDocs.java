package com.co.solia.emotional.emotional.controllers.docs;

import com.co.solia.emotional.emotional.models.dtos.rq.EmotionalRqDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalRsDto;
import com.co.solia.emotional.emotional.models.dtos.rq.EmotionalBatchRqDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalBatchRsDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalUniqueRsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

/**
 * Interface specialized to doc endpoints.
 *
 * @author luis.bolivar
 */
@Tag(name = "Emotional", description = "compute messages to get emotional estimations.")
public interface EmotionalControllerDocs {

    /**
     * estimate the emotional message.
     * @param emotionalMessage to process.
     * @return {@link ResponseEntity} of {@link EmotionalRsDto}.
     */
    @Operation(
            summary = "estimate a single message with emotional analysis.",
            description = "estimate a single message to get a list of emotions, which one with values between 0 to 1, where 0 means is not close to this " +
                    "feeling and 1 to totally this feeling."
    )
    ResponseEntity<EmotionalRsDto> compute(EmotionalRqDto emotionalMessage);

    /**
     * get an emotional estimated by id.
     * @param id to get emotional estimation.
     * @return {@link ResponseEntity} of {@link EmotionalRsDto}.
     */
    @Operation(
            summary = "get a previous emotional processing by id.",
            description = "get a previous emotional processing by id without to process again."
    )
    @Parameters({
            @Parameter(
                    name = "id",
                    description = "the id emotional processing.",
                    example = "b6fbb344-da94-4433-b3f6-d67540d32ccc",
                    in = ParameterIn.PATH),
    })
    ResponseEntity<EmotionalRsDto> getById(UUID id);

    /**
     * compute a list of emotional estimations.
     * @param messages to compute.
     * @return {@link ResponseEntity} of {@link EmotionalBatchRsDto}.
     */
    @Operation(
            summary = "compute a list of message to get estimation.",
            description = "compute a list of messages to get estimation for each message."
    )
    ResponseEntity<EmotionalBatchRsDto> computeBatch(EmotionalBatchRqDto messages);

    /**
     * process a list of messages in emotional estimation for all list.
     * @param messages to process.
     * @return {@link ResponseEntity} of {@link EmotionalBatchRsDto}.
     */
    @Operation(
            summary = "compute a list of message to get estimation.",
            description = "compute a list of messages to get estimation for all messages."
    )
    ResponseEntity<EmotionalUniqueRsDto> computeUnique(EmotionalBatchRqDto messages);

    /**
     * get batch processing by identifier.
     * @param id batch processing identifier.
     * @return {@link ResponseEntity} of {@link EmotionalBatchRsDto}.
     */
    @Operation(
            summary = "get a batch processing by identifier.",
            description = "get a batch processing by identifier without to process again."
    )
    @Parameters({
            @Parameter(
                    name = "id",
                    description = "the id of batch emotional processing.",
                    example = "b6fbb344-da94-4433-b3f6-d67540d32ccc",
                    in = ParameterIn.PATH),
    })
    ResponseEntity<EmotionalBatchRsDto> getByBatchECId(UUID id);

    /**
     * get unique processing by identifier.
     * @param id unique processing identifier.
     * @return {@link ResponseEntity} of {@link EmotionalBatchRsDto}.
     */
    @Operation(
            summary = "get a unique processing by identifier.",
            description = "get a unique processing by identifier without to process again."
    )
    @Parameters({
            @Parameter(
                    name = "id",
                    description = "the id of unique emotional processing.",
                    example = "b6fbb344-da94-4433-b3f6-d67540d32ccc",
                    in = ParameterIn.PATH),
    })
    ResponseEntity<EmotionalUniqueRsDto> getByUniqueId(UUID id);
}
