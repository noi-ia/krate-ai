package com.co.solia.emotional.campaign.controllers.docs;

import com.co.solia.emotional.campaign.models.dtos.rq.CampaignRqDto;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;
import org.springframework.http.ResponseEntity;

/**
 * documentation of campaign endpoints.
 *
 * @author luis.bolivar.
 */
public interface CampaignControllerDoc {

    /**
     * process a campaign.
     * @param request to generate the campaign.
     * @return {@link ResponseEntity} of {@link CampaignRsDto}.
     */
    ResponseEntity<CampaignRsDto> compute(CampaignRqDto request);
}
