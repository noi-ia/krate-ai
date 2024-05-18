package com.co.solia.emotional.clean.models.dtos.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * class to map the response of cleaning.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CleanBatchRsDto {

    /**
     * id batch of processing.
     */
    private UUID id;

    /**
     * list of messages cleaned.
     */
    private List<Map<String, String>> results;
}
