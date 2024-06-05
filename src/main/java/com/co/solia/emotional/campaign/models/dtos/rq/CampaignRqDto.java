package com.co.solia.emotional.campaign.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
     * list of messages.
     */
    private List<String> messages;
}
