package com.co.solia.emotional.campaign.models.dtos.rs;

import com.co.solia.emotional.campaign.models.dtos.dtos.PillarDto;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

/**
 * response of campaign result.
 * @param id campaign identifier.
 * @param name name of the campaign-
 * @param description description of the campaign.
 * @param pillars of the campaign.
 * @author luis.bolivar.
 */
@Builder
public record CampaignRsDto (
        UUID id,
        String name,
        String description,
        List<PillarDto> pillars
){
}
