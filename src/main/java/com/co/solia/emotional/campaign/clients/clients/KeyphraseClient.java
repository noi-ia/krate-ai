package com.co.solia.emotional.campaign.clients.clients;

import com.co.solia.emotional.campaign.models.dtos.rq.KeyphraseClientRqDto;
import com.co.solia.emotional.campaign.models.dtos.rs.KeyphraseClientRsDto;

import java.util.List;
import java.util.Optional;

/**
 * interface of keyphrase call.
 *
 * @author luis.bolivar.
 */
public interface KeyphraseClient {

    /**
     * generate the keyphrases associated to the messages.
     * @param request to call the keyphrases.
     * @param emotion to generate the keyphrases.
     * @return {@link Optional} of {@link KeyphraseClientRsDto}.
     */
    Optional<KeyphraseClientRsDto> generateKeyphrases(KeyphraseClientRqDto request, String emotion);

    /**
     * generate the keyphrases associated to the messages.
     * @param messages to call the keyphrases.
     * @param emotion to generate the keyphrases.
     * @return {@link Optional} of {@link KeyphraseClientRsDto}.
     */
    Optional<KeyphraseClientRsDto> generateKeyphrases(List<String> messages, String emotion);
}
