package com.co.solia.emotional.keyphrase.services.services;

import com.co.solia.emotional.keyphrase.models.daos.KeyphraseDao;
import com.co.solia.emotional.keyphrase.models.daos.KeyphrasesDao;
import com.co.solia.emotional.keyphrase.models.dtos.rq.KeyphraseRqDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphraseRsDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphrasesRsDto;
import com.co.solia.emotional.keyphrase.models.enums.EmotionEnum;

import java.util.List;
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
     * @return {@link Optional} of {@link KeyphrasesRsDto}.
     */
    Optional<KeyphrasesRsDto> compute(KeyphraseRqDto keyphraseRq, EmotionEnum emotion);

    /**
     * get a keyphrases process by id.
     * @param id to get the keyphrase.
     * @return {@link Optional} of {@link KeyphrasesRsDto}.
     */
    Optional<KeyphrasesRsDto> getById(UUID id);

    /**
     * save the keyphrases.
     * @param keyphrases to save.
     * @return {@link Optional} of {@link KeyphrasesDao}.
     */
    Optional<KeyphrasesDao> saveKeyphrase(final KeyphrasesDao keyphrases);

    /**
     * save the keyphrase.
     * @param keyphrase to save.
     * @return {@link Optional} of {@link KeyphraseDao}.
     */
    Optional<KeyphraseDao> saveKeyphrase(final KeyphraseDao keyphrase);

    /**
     * get a {@link List} of {@link KeyphraseDao} by keyphrases id.
     * @param id to get the {@link List} of {@link KeyphraseDao}.
     * @return {@link Optional} of {@link List} of {@link KeyphraseDao}.
     */
    Optional<List<KeyphraseDao>> getKeyPhrasesById(UUID id);

    /**
     * get a keyphrase process by id.
     * @param id to get the keyphrase.
     * @return {@link Optional} of {@link KeyphraseRsDto}.
     */
    Optional<KeyphraseRsDto> getKeyphraseById(UUID id);
}
