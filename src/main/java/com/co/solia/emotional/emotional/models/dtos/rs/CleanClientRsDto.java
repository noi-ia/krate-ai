package com.co.solia.emotional.emotional.models.dtos.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

/**
 * the client response to call the clean service.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CleanClientRsDto {
    /**
     * id of processing.
     */
    private UUID id;

    /**
     * list of messages cleaned.
     */
    private Map<String, String> result;
}
