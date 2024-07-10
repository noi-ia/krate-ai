package com.co.solia.emotional.campaign.controllers.docs;

import com.co.solia.emotional.campaign.models.dtos.rq.CampaignRqDto;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

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

    /**
     * ge a campaign by id.
     * @param id tto get the campaign.
     * @return {@link ResponseEntity} of {@link CampaignRsDto}.
     */
    ResponseEntity<CampaignRsDto> getById(final UUID id);
}
