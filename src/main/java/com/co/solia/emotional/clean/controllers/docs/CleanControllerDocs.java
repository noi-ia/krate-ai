package com.co.solia.emotional.clean.controllers.docs;

import com.co.solia.emotional.clean.models.dtos.rq.CleanRqDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanRsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

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
}
