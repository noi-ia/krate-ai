package com.co.solia.emotional.clean.services.services;

import com.co.solia.emotional.clean.models.daos.CleanDao;
import com.co.solia.emotional.clean.models.dtos.rq.CleanRqDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanRsDto;

import java.util.Optional;

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
}
