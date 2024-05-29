package com.co.solia.emotional.keyphrase.clients.clients;

import com.co.solia.emotional.keyphrase.models.dtos.rq.EmotionalClientRqDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.EmotionalClientRsDto;

import java.util.Optional;

/**
 * Interface to map the calls to emotional api.
 *
 * @author luis.bolivar.
 */
public interface EmotionalClient {

    /**
     * compute the emotions from a list of messages
     * @param emotionalRq with the messages.
     * @return {@link Optional} of {@link EmotionalClientRsDto}.
     */
    Optional<EmotionalClientRsDto> compute(EmotionalClientRqDto emotionalRq);
}
