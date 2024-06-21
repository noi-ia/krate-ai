package com.co.solia.emotional.share.clients.clients;

import com.co.solia.emotional.keyphrase.models.dtos.rq.EmotionalClientRqDto;
import com.co.solia.emotional.share.models.dtos.rs.EmotionalClientRsDto;

import java.util.Optional;
import java.util.UUID;

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

    /**
     * get a emotional processing by id.
     * @param id to get the emotional processing.
     * @return {@link Optional} of {@link EmotionalClientRsDto}.
     */
    Optional<EmotionalClientRsDto> getById(UUID id);
}
