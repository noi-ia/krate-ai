package com.co.solia.emotional.keyphrase.services.services;

import com.co.solia.emotional.keyphrase.models.dtos.rq.KeyphraseRqDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphraseRsDto;
import com.co.solia.emotional.keyphrase.models.enums.EmotionEnum;

import java.util.Optional;
import java.util.UUID;

/**
 * the interface of {@link KeyphraseService}.
 *
 * @author luis.bolivar.
 */
public interface KeyphraseService {

    /**
     * compute the keyphrase.
     * @param keyphraseRq request to compute.
     * @return {@link Optional} of {@link KeyphraseRsDto}.
     */
    Optional<KeyphraseRsDto> compute(KeyphraseRqDto keyphraseRq, EmotionEnum emotion);

    /**
     * get a keyphrase process by id.
     * @param id to get the keyphrase.
     * @return {@link Optional} of {@link KeyphraseRsDto}.
     */
    Optional<KeyphraseRsDto> getById(UUID id);
}
