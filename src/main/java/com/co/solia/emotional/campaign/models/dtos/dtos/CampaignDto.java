package com.co.solia.emotional.campaign.models.dtos.dtos;

import lombok.Builder;

import java.util.List;

/**
 * the basic structure of a campaign.
 * @param name the name of the campaign.
 * @param description the description of the campaign.
 * @param pillars the pillars of the campaign.
 */
@Builder
public record CampaignDto(
        String name,
        String description,
        List<PillarDto> pillars
) {
}
