package com.co.solia.emotional.campaign.models.mappers;

import com.co.solia.emotional.campaign.models.daos.CampaignDao;
import com.co.solia.emotional.campaign.models.dtos.dtos.CampaignDto;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;
import com.co.solia.emotional.share.models.validators.BasicValidator;
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

    /**
     * build the campaign dao: {@link CampaignDao}.
     * @param chat to get the campaign result.
     * @param duration duration of the process.
     * @param id identifier of the campaign.
     * @param keyphrase related to the campaign.
     * @param brandId brand identifier.
     * @param userId user identifier.
     * @param emotionsId emotion estimation identifier.
     * @return {@link Optional} of {@link CampaignDao}.
     */
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
                .flatMap(result -> buildCampaign(result, duration, id, keyphrase, brandId, userId, emotionsId));
    }

    /**
     * build the campaign dao: {@link CampaignDao}.
     * @param chat to get the campaign result.
     * @param duration duration of the process.
     * @param id identifier of the campaign.
     * @param keyphrase related to the campaign.
     * @param brandId brand identifier.
     * @param userId user identifier.
     * @param emotionsId emotion estimation identifier.
     * @return {@link Optional} of {@link CampaignDao}.
     */
    private static Optional<CampaignDao> buildCampaign(
            final ChatCompletion chat,
            final Long duration,
            final UUID id,
            final String keyphrase,
            final UUID brandId, final UUID userId,
            final UUID emotionsId) {
        return getCampaign(chat).map(campaign -> CampaignDao.builder()
                .id(id)
                .keyphrase(keyphrase)
                .name(campaign.name())
                .description(campaign.description())
                .pillars(campaign.pillars())
                .brandId(brandId)
                .idUser(userId)
                .duration(duration)
                .fingerPrintOpenai(chat.systemFingerprint())
                .tokens(getTokens(chat))
                .emotionsId(emotionsId)
                .openAiId(chat.id())
                .build());
    }

    /**
     * get the campaign from the openai result.
     * @param chat to get the campaign.
     * @return {@link Optional} of {@link CampaignDto}.
     */
    private static Optional<CampaignDto> getCampaign(final ChatCompletion chat) {
        Optional<CampaignDto> result = Optional.empty();
        try {
            final String text = chat.choices().get(0).message().content();
            final CampaignDto campaign = new Gson().fromJson(text, CampaignDto.class);
            result = campaign != null ? Optional.of(campaign): Optional.empty();
        } catch (Exception e) {
            log.error("[getGetCampaign]: error getting campaign result: {}", e.getMessage());
        }
        return result;
    }

    /**
     * get a {@link CampaignRsDto} from {@link CampaignDao}.
     * @param dao to get the {@link CampaignRsDto}.
     * @return {@link Optional} of {@link CampaignRsDto}.
     */
    public static Optional<CampaignRsDto> getRsFromDao(final CampaignDao dao) {
        return Stream.of(dao)
                .filter(Objects::nonNull)
                .filter(d -> BasicValidator.isValidId(d.getId()))
                .filter(d -> BasicValidator.isValidString(d.getName()))
                .findFirst()
                .map(CampaignMapper::getRsFromCampaignDao);
    }

    /**
     * get a {@link CampaignRsDto} from {@link CampaignDao}.
     * @param dao to get the {@link CampaignRsDto}.
     * @return {@link CampaignRsDto}.
     */
    private static CampaignRsDto getRsFromCampaignDao(final CampaignDao dao) {
        return CampaignRsDto.builder()
                .id(dao.getId())
                .name(dao.getName())
                .description(dao.getDescription())
                .pillars(dao.getPillars())
                .build();
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
