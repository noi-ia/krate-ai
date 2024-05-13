package com.co.solia.emotional.emotional.controllers.docs;

import com.co.solia.emotional.emotional.models.dtos.rq.EmotionalRqDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalRsDto;
import com.co.solia.emotional.emotional.models.dtos.rq.EmotionalBatchRqDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalBatchRsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Interface specialized to doc endpoints.
 *
 * @author luis.bolivar
 */
public interface EmotionalControllerDocs {

    /**
     * estimate the emotional message.
     * @param emotionalMessage to process.
     * @return {@link ResponseEntity} of {@link EmotionalRsDto}.
     */
    ResponseEntity<EmotionalRsDto> compute(@RequestBody final EmotionalRqDto emotionalMessage);

    /**
     * get an emotional estimated by id.
     * @param idEE to get emotional estimation.
     * @return {@link ResponseEntity} of {@link EmotionalRsDto}.
     */
    ResponseEntity<EmotionalRsDto> getById(@PathVariable("idEE") final UUID idEE);

    /**
     * compute a list of emotional estimations.
     * @param messages to compute.
     * @return {@link ResponseEntity} of {@link EmotionalBatchRsDto}.
     */
    ResponseEntity<EmotionalBatchRsDto> computeList(@RequestBody final EmotionalBatchRqDto messages);
}
