package com.co.solia.emotional.keyphrase.models.dtos.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * the response to the unique emotional compute in messages.in keyphrase
 *
 * @author luis.bolivar
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class EmotionalDto {
    /**
     * the unique emotional compute id.
     */
    private UUID id;

    /**
     * emotions results from estimation.
     */
    private Map<String, Double> emotions;
}
