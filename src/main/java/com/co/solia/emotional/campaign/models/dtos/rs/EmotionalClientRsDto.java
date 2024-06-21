package com.co.solia.emotional.campaign.models.dtos.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * response from emotional call.
 *
 * @author luis.bolivar.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class EmotionalClientRsDto {

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
