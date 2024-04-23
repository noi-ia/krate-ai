package com.co.solia.emotional.emotional.services.services;

import com.co.solia.emotional.emotional.models.daos.EmotionalEstimationDao;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRqDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRsDto;

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
     * @param emotionalMessage message to process
     * @return {@link Optional} of {@link EmotionalMessageRsDto}.
     */
    Optional<EmotionalMessageRsDto> estimate(EmotionalMessageRqDto emotionalMessage);

    /**
     * save the emotional estimation.
     * @param emotionalEstimation to save
     */
    void save(EmotionalEstimationDao emotionalEstimation);


    /**
     * get emotional estimation by id.
     * @param id to get emotional estimation.
     * @return {@link Optional} of {@link EmotionalMessageRsDto}.
     */
    Optional<EmotionalMessageRsDto> getById(UUID id);
}
