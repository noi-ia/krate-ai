package com.co.solia.emotional.emotional.clients.clients;

import com.co.solia.emotional.emotional.models.dtos.rs.CleanClientRsDto;

import java.util.Optional;

/**
 * Interface for cleaning calls.
 *
 * @author luis.bolivar.
 */
public interface CleanClient {

    /**
     * call clean message.
     * @param message to clean.
     * @return {@link Optional} of {@link CleanClientRsDto}.
     */
    Optional<CleanClientRsDto> cleanMessage(String message);

    /**
     * clean a message.
     * @param message to clean.
     * @return {@link String} message clean.
     */
    Optional<String> clean(String message);
}
