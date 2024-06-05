package com.co.solia.emotional.keyphrase.controllers.docs;

import com.co.solia.emotional.keyphrase.models.dtos.rq.KeyphraseRqDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphraseRsDto;
import com.co.solia.emotional.keyphrase.models.enums.EmotionEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

/**
 * documentation for keyphrase.
 *
 * @author luis.bolivar.
 */
@Tag(name = "Keyphrase", description = "compute messages to get the keyphrases.")
public interface KeyphraseControllerDocs {

    /**
     * get the keyphrase by id.
     * @param id to get the keyphrase.
     * @return {@link ResponseEntity} of {@link KeyphraseRsDto}.
     */
    @Operation(
            summary = "get a previous keyphrase processing by id.",
            description = "get a previous keyphrase processing by id without to process again."
    )
    @Parameters({
            @Parameter(
                    name = "id",
                    description = "the id keyphrase processing.",
                    example = "b6fbb344-da94-4433-b3f6-d67540d32ccc",
                    required = true,
                    in = ParameterIn.PATH),
    })
    ResponseEntity<KeyphraseRsDto> getById(UUID id);

    /**
     * compute the keyphrase.
     * @param keyphraseRq request to process.
     * @param emotion to process the messages.
     * @return {@link ResponseEntity} of {@link KeyphraseRsDto}.
     */
    @Operation(
            summary = "estimate the keyphrases associated to the messages.",
            description = "estimate the keyphrases associated to the messages."
    )
    @Parameters({
            @Parameter(
                    name = "emotion",
                    description = "emotion to get the keyphrases.",
                    example = "FELICIDAD",
                    required = true,
                    in = ParameterIn.PATH),
    })
    ResponseEntity<KeyphraseRsDto> compute(KeyphraseRqDto keyphraseRq, EmotionEnum emotion);
}
