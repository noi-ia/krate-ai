package com.co.solia.emotional.campaign.models.mappers;

import com.co.solia.emotional.campaign.models.daos.CampaignDao;
import com.co.solia.emotional.campaign.models.dtos.dtos.CampaignDto;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;
import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * class to map the all resources about the campaign.
 *
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class CampaignMapper {

    public static Optional<CampaignDao> fromChatGetDao(
            final ChatCompletion chat,
            final Long duration,
            final UUID id,
            final String keyphrase,
            final UUID brandId,
            final UUID userId,
            final UUID emotionsId) {
        return Stream.of(chat)
                .filter(Objects::nonNull)
                .filter(result -> result.choices() != null)
                .filter(result -> !result.choices().isEmpty())
                .findFirst()
                .map(result -> buildCampaign(result, duration, id, keyphrase, brandId, userId, emotionsId));
    }

    private static CampaignDao buildCampaign(
            final ChatCompletion chat,
            final Long duration,
            final UUID id,
            final String keyphrase,
            final UUID brandId, final UUID userId,
            final UUID emotionsId) {
        final String text = chat.choices().get(0).message().content();
        final CampaignDto campaign = new Gson().fromJson(text, CampaignDto.class);
        return CampaignDao.builder()
                .id(id)
                .keyphrase(keyphrase)
                .brandId(brandId)
                .campaign(text)
                .idUser(userId)
                .duration(duration)
                .fingerPrintOpenai(chat.systemFingerprint())
                .tokens(getTokens(chat))
                .emotionsId(emotionsId)
                .openAiId(chat.id())
                .build();
    }

    public static Optional<CampaignRsDto> getRsFromDao(final CampaignDao campaignDao) {
        return Optional.empty();
    }

    /**
     * get the tokens used generating the keyphrases.
     * @param chat to get the tokens.
     * @return {@link Map}, {@code key}: {@link String} type of token, {@code value}: {@link Integer} amount of tokens.
     */
    private static Map<String, Integer> getTokens(final ChatCompletion chat) {
        return Map.of("promptTokens", chat.usage().promptTokens(),
                "totalTokens", chat.usage().totalTokens(),
                "completionTokens", chat.usage().completionTokens());
    }
}
