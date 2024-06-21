package com.co.solia.emotional.campaign.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
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
     * keyphrase to generate the campaign.
     */
    private String keyphrase;

    /**
     * brand identifier to add context to the campaign.
     */
    private UUID brandId;

    /**
     * emotional estimation identifier to add context to the campaign.
     */
    private UUID emotionalId;
}
