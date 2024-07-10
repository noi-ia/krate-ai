package com.co.solia.emotional.campaign.services.services;

import com.co.solia.emotional.campaign.models.daos.CampaignDao;
import com.co.solia.emotional.campaign.models.dtos.rq.CampaignRqDto;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface to map the services of campaign.
 *
 * @author luis.bolivar.
 */
public interface CampaignService {

    /**
     * compute for generate the campaigns.
     * @param request to generate the campaign.
     * @return {@link Optional} of {@link CampaignRsDto}.
     */
    Optional<CampaignRsDto> compute(CampaignRqDto request);

    /**
     * save a campaign.
     * @param dao campaign dao to save.
     * @return {@link Optional} of {@link CampaignDao}.
     */
    Optional<CampaignDao> save(CampaignDao dao);

    /**
     * get campaign by id.
     * @param id to get the campaign.
     * @return {@link Optional} of {@link CampaignRsDto}.
     */
    Optional<CampaignRsDto> getById(UUID id);
}
