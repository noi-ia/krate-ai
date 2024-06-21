package com.co.solia.emotional.campaign.clients.clients;

import com.co.solia.emotional.campaign.models.dtos.rs.EmotionalClientRsDto;

import java.util.Optional;
import java.util.UUID;

/**
 * interface to map the emotional services.
 *
 * @author luis.bolivar.
 */
public interface EmotionalClient {

    /**
     * get a emotional processing by id.
     * @param id to get the emotional processing.
     * @return {@link Optional} of {@link EmotionalClientRsDto}.
     */
    Optional<EmotionalClientRsDto> getById(UUID id);
}
