package com.co.solia.emotional.campaign.controllers.endpoints.v1;

import com.co.solia.emotional.campaign.controllers.docs.CampaignControllerDoc;
import com.co.solia.emotional.campaign.models.dtos.rq.CampaignRqDto;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;
import com.co.solia.emotional.campaign.services.services.CampaignService;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * campaign endpoints.
 *
 * @author luis.bolivar
 */
@RestController
@RequestMapping("/1/campaign")
@AllArgsConstructor
public class CampaignController implements CampaignControllerDoc {

    /**
     * dependency on {@link CampaignService}.
     */
    private CampaignService campaignService;

    /**
     * {@inheritDoc}.
     * @param request to generate the campaign.
     * @return
     */
    @PostMapping("/compute/")
    @Override
    public ResponseEntity<CampaignRsDto> compute(@RequestBody final CampaignRqDto request) {
        return campaignService.compute(request, "amor")
                .map(ResponseEntity::ok)
                .orElseThrow(() -> InternalServerException.builder()
                        .message("The compute process failed, try again.")
                        .endpoint("/campaign/compute/").build());
    }
}
