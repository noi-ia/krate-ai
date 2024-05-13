package com.co.solia.emotional.emotional.services.services;

import com.co.solia.emotional.emotional.models.daos.EmotionalBatchDao;
import com.co.solia.emotional.emotional.models.daos.EmotionalDao;
import com.co.solia.emotional.emotional.models.daos.EmotionalUniqueDao;
import com.co.solia.emotional.emotional.models.dtos.rq.EmotionalRqDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalRsDto;
import com.co.solia.emotional.emotional.models.dtos.rq.EmotionalBatchRqDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalBatchRsDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalUniqueRsDto;

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
     * @return {@link Optional} of {@link EmotionalRsDto}.
     */
    Optional<EmotionalRsDto> compute(EmotionalRqDto message);

    /**
     * estimate message list with emotional process behaviors.
     * @param messages messages to process
     * @return {@link Optional} of {@link EmotionalBatchRsDto}.
     */
    Optional<EmotionalBatchRsDto> computeList(EmotionalBatchRqDto messages);

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
     * save the emotional unique process result.
     * @param emotionalUnique to save.
     */
    void save(EmotionalUniqueDao emotionalUnique);

    /**
     * get emotional estimation by id.
     * @param id to get emotional estimation.
     * @return {@link Optional} of {@link EmotionalRsDto}.
     */
    Optional<EmotionalRsDto> getById(UUID id);

    /**
     * get a messages processed by batch identifier.
     * @param id batch emotional compute identifier.
     * @return {@link Optional} of {@link EmotionalBatchRsDto}.
     */
    Optional<EmotionalBatchRsDto> getByBatchId(UUID id);

    /**
     * compute unique emotional in messages.
     * @param emotionalBatch messages to have a unique process.
     * @return {@link Optional} of {@link EmotionalUniqueRsDto}.
     */
    Optional<EmotionalUniqueRsDto> computeUnique(EmotionalBatchRqDto emotionalBatch);

    /**
     * get a messages processed by unique identifier.
     * @param id unique emotional compute identifier.
     * @return {@link Optional} of {@link EmotionalUniqueRsDto}.
     */
    Optional<EmotionalUniqueRsDto> getByUniqueId(UUID id);
}
