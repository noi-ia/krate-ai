package com.co.solia.emotional.emotional.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * response body of emotional estimation.
 *
 * @author luis.bolivar
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmotionalMessageRsDto {

    /**
     * emotional estimation id.
     */
    private UUID eeId;

    /**
     * message processed.
     */
    private String message;

    /**
     * emotions related to estimation.
     */
    private Map<String, Double> emotions;
}
