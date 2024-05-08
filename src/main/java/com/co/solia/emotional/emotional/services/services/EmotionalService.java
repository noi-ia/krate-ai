package com.co.solia.emotional.emotional.services.services;

import com.co.solia.emotional.emotional.models.daos.EmotionalBatchDao;
import com.co.solia.emotional.emotional.models.daos.EmotionalDao;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRqDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRsDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessagesRqDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessagesRsDto;

import java.util.Optional;
import java.util.UUID;

/**
 * Service to map the emotional process behaviors.
 *
 * @author luis.bolivar
 */
public interface EmotionalService {

    /**
     * estimate message with emotional process behaviors.
     * @param message message to process
     * @return {@link Optional} of {@link EmotionalMessageRsDto}.
     */
    Optional<EmotionalMessageRsDto> compute(EmotionalMessageRqDto message);

    /**
     * estimate message list with emotional process behaviors.
     * @param messages messages to process
     * @return {@link Optional} of {@link EmotionalMessagesRsDto}.
     */
    Optional<EmotionalMessagesRsDto> computeList(EmotionalMessagesRqDto messages);

    /**
     * save the emotional estimation.
     * @param emotionalEstimation to save
     */
    void save(EmotionalDao emotionalEstimation);

    /**
     * save the emotional batch compute.
     * @param emotionalBatch to save.
     */
    void save (EmotionalBatchDao emotionalBatch);

    /**
     * get emotional estimation by id.
     * @param id to get emotional estimation.
     * @return {@link Optional} of {@link EmotionalMessageRsDto}.
     */
    Optional<EmotionalMessageRsDto> getById(UUID id);

    /**
     * get a messages processed by batch identifier.
     * @param beeId batch emotional estimation identifier.
     * @return {@link Optional} of {@link EmotionalMessagesRsDto}.
     */
    Optional<EmotionalMessagesRsDto> getECByBatchId(UUID beeId);
}
