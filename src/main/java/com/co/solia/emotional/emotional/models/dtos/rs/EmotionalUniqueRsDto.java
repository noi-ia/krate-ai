package com.co.solia.emotional.emotional.models.dtos.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * the response to the unique emotional compute in messages.
 *
 * @author luis.bolivar
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class EmotionalUniqueRsDto {

    /**
     * the unique emotional compute id.
     */
    private UUID id;

    /**
     * messages processed.
     */
    private List<String> messages;

    /**
     * emotions results from estimation.
     */
    private Map<String, Double> emotions;
}
