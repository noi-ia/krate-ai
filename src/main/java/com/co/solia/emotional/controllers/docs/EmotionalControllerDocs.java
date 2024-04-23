package com.co.solia.emotional.controllers.docs;

import com.co.solia.emotional.models.dtos.EmotionalMessageRqDto;
import com.co.solia.emotional.models.dtos.EmotionalMessageRsDto;
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
     * @return {@link ResponseEntity} of {@link EmotionalMessageRsDto}.
     */
    ResponseEntity<EmotionalMessageRsDto> estimate(@RequestBody final EmotionalMessageRqDto emotionalMessage);

    /**
     * get an emotional estimated by id.
     * @param idEE to get emotional estimation.
     * @return {@link ResponseEntity} of {@link EmotionalMessageRsDto}.
     */
    ResponseEntity<EmotionalMessageRsDto> getById(@PathVariable("idEE") final UUID idEE);
}
