package com.co.solia.emotional.campaign.controllers.endpoints.v1;

import com.co.solia.emotional.campaign.controllers.docs.CampaignControllerDoc;
import com.co.solia.emotional.campaign.models.dtos.rq.CampaignRqDto;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;
import com.co.solia.emotional.campaign.services.services.CampaignService;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.models.validators.ServiceValidator;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * campaign endpoints.
 *
 * @author luis.bolivar
 */
@RestController
@RequestMapping("/1/campaign")
@AllArgsConstructor
@Slf4j
public class CampaignController implements CampaignControllerDoc {

    /**
     * dependency on {@link CampaignService}.
     */
    private CampaignService campaignService;

    /**
     * {@inheritDoc}.
     * @param rq to generate the campaign.
     * @return
     */
    @PostMapping("/compute/")
    @Override
    public ResponseEntity<CampaignRsDto> compute(@RequestBody final CampaignRqDto rq) {
        ServiceValidator.validateCampaignRq(rq);
        return campaignService.compute(rq)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[compute]: The compute process failed, try again.");
                    throw InternalServerException.builder()
                            .message("The compute process failed, try again.")
                            .endpoint("/campaign/compute/").build();
                });
    }

    /**
     * get a campaign by id.
     * @param id tto get the campaign.
     * @return {@link ResponseEntity} of {@link CampaignRsDto}.
     */
    @GetMapping("/compute/{id}")
    @Override
    public ResponseEntity<CampaignRsDto> getById(@PathVariable("id") final UUID id) {
        ServiceValidator.validateId(id, "/campaign/compute/{id}");
        return campaignService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[getById]: error getting the campaign by id: {}.", id);
                    throw InternalServerException.builder()
                            .message("error getting the campaign by id: " + id)
                            .endpoint("/campaign/compute/{id}").build();
                });
    }

}
