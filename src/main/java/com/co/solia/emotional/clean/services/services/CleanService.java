package com.co.solia.emotional.clean.services.services;

import com.co.solia.emotional.clean.models.daos.CleanBatchDao;
import com.co.solia.emotional.clean.models.daos.CleanDao;
import com.co.solia.emotional.clean.models.dtos.rq.CleanBatchRqDto;
import com.co.solia.emotional.clean.models.dtos.rq.CleanRqDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanBatchRsDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanRsDto;

import java.util.Optional;
import java.util.UUID;

/**
 * clean service interface.
 *
 * @author luis.bolivar.
 */
public interface CleanService {

    /**
     * clean a message;
     * @param cleanRq with the message to clean.
     * @return {@link Optional} of {@link CleanRsDto}.
     */
    Optional<CleanRsDto> clean(CleanRqDto cleanRq);

    /**
     * save the cleaning process.
     * @param clean to save.
     */
    void save(CleanDao clean);

    /**
     * get a {@link CleanRsDto} by id.
     * @param id identifier of cleaning process.
     * @return {@link Optional} of {@link CleanRsDto}.
     */
    Optional<CleanRsDto> getById(UUID id);

    /**
     * clean a message;
     * @param cleanListRq with the messages to clean.
     * @return {@link Optional} of {@link CleanRsDto}.
     */
    Optional<CleanBatchRsDto> cleanList(CleanBatchRqDto cleanListRq);

    /**
     * save the cleaning batch process.
     * @param clean to save.
     */
    void save(CleanBatchDao clean);

    /**
     * get a {@link CleanBatchRsDto} by id.
     * @param id identifier of cleaning batch process.
     * @return {@link Optional} of {@link CleanRsDto}.
     */
    Optional<CleanBatchRsDto> getByBatchId(UUID id);
}
