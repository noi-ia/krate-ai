package com.co.solia.emotional.clean.controllers.docs;

import com.co.solia.emotional.clean.models.dtos.rq.CleanBatchRqDto;
import com.co.solia.emotional.clean.models.dtos.rq.CleanRqDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanBatchRsDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanRsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Interface to separate the documentation responsibility.
 *
 * @author luis.bolivar.
 */
@Tag(name = "Clean", description = "endpoints to clean messages.")
public interface CleanControllerDocs {

    /**
     * clean a message.
     * @param cleanRq message to clean.
     * @return {@link ResponseEntity} of {@link CleanRsDto}.
     */
    @Operation(
            summary = "clean a message.",
            description = "the basic clean message to improve the data processing."
    )
    ResponseEntity<CleanRsDto> clean(CleanRqDto cleanRq);

    /**
     * get {@link CleanRsDto} by id.
     * @param id cleaning identifier.
     * @return {@link ResponseEntity} of {@link CleanRsDto}.
     */
    @Operation(
            summary = "get clean a message by id.",
            description = "the basic get cleaned message by id."
    )
    ResponseEntity<CleanRsDto> getById(final UUID id);

    /**
     * get {@link CleanRsDto} by id.
     * @param id cleaning batch identifier.
     * @return {@link ResponseEntity} of {@link CleanRsDto}.
     */
    @Operation(
            summary = "get clean batch by id.",
            description = "the basic get cleaned batch messages by id."
    )
    ResponseEntity<CleanBatchRsDto> getByBatchId(final UUID id);

    /**
     * clean messages.
     * @param cleanRq messages to clean.
     * @return {@link ResponseEntity} of {@link CleanBatchRsDto}.
     */
    @Operation(
            summary = "clean messages.",
            description = "the basic clean messages to improve the data processing."
    )
    ResponseEntity<CleanBatchRsDto> cleanList(final CleanBatchRqDto cleanRq);
}
