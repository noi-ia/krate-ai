package com.co.solia.emotional.campaign.clients.clients;

import com.co.solia.emotional.campaign.models.dtos.rs.KeyphraseClientRsDto;

import java.util.Optional;
import java.util.UUID;

/**
 * interface to map the call to Keyphrase component.
 *
 * @author luis.bolivar.
 */
public interface KeyphraseClient {

    /**
     * get the keyphrase by id.
     * @param id to get the keyphrase.
     * @return {@link Optional} of {@link KeyphraseClientRsDto}.
     */
    Optional<KeyphraseClientRsDto> getKeyphraseById(UUID id);
}
