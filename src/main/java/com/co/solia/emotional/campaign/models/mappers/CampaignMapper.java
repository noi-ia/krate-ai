package com.co.solia.emotional.campaign.models.mappers;

import com.co.solia.emotional.campaign.models.daos.CampaignDao;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;

import java.nio.channels.FileChannel;
import java.util.Optional;

/**
 * class to map the all resources about the campaign.
 *
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class CampaignMapper {

    public static Optional<CampaignDao> fromChatGetDao(final ChatCompletion chat, final Long duration) {
        return Optional.empty();
    }

    public static Optional<CampaignRsDto> getRsFromDao(final CampaignDao campaignDao) {
        return Optional.empty();
    }
}
