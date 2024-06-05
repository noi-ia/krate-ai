package com.co.solia.emotional.campaign.models.dtos.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * response of campaign result.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CampaignRsDto {

    /**
     * list of messages.
     */
    private List<String> results;
}
