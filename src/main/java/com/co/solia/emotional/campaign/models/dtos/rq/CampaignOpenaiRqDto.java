package com.co.solia.emotional.campaign.models.dtos.rq;

import com.co.solia.emotional.campaign.models.dtos.dtos.BrandDto;
import lombok.Builder;

import java.util.Map;

/**
 * the campaign dto to pass to openaiService.
 * @param keyphrase to get the campaign.
 * @param brand to generate the campaign.
 * @param emotions emotions associated with the comments.
 */
@Builder
public record CampaignOpenaiRqDto(
        String keyphrase,
        BrandDto brand,
        Map<String, Double> emotions
) {
}
