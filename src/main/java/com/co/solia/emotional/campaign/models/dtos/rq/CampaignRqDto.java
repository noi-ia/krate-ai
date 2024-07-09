package com.co.solia.emotional.campaign.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * request to generate campaigns.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CampaignRqDto {

    /**
     * keyphrase id to generate the campaign.
     */
    private UUID keyphraseId;

    /**
     * brand identifier to add context to the campaign.
     */
    private UUID brandId;

    /**
     * emotional estimation identifier to add context to the campaign.
     */
    private UUID emotionalId;
}
