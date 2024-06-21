package com.co.solia.emotional.campaign.services.impls;

import com.co.solia.emotional.campaign.clients.clients.BrandClient;
import com.co.solia.emotional.campaign.models.daos.CampaignDao;
import com.co.solia.emotional.campaign.models.dtos.rq.CampaignRqDto;
import com.co.solia.emotional.campaign.models.dtos.rs.BrandClientRsDto;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;
import com.co.solia.emotional.campaign.models.mappers.CampaignMapper;
import com.co.solia.emotional.campaign.models.repos.CampaignRepo;
import com.co.solia.emotional.campaign.services.services.CampaignService;
import com.co.solia.emotional.share.clients.clients.EmotionalClient;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.models.validators.BasicValidator;
import com.co.solia.emotional.share.services.services.OpenAIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * implementation of {@link CampaignService}.
 *
 * @author luis.bolivar.
 */
@Service
@Slf4j
@AllArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    /**
     * dependency on {@link OpenAIService}.
     */
    private OpenAIService openAIService;

    /**
     * dependency on {@link CampaignRepo}.
     */
    private CampaignRepo campaignRepo;

    /**
     * dependency on {@link EmotionalClient}.
     */
    private EmotionalClient emotionalClient;

    /**
     * dependency on {@link BrandClient}.
     */
    private BrandClient brandClient;

    /**
     * compute for generate the campaigns.
     * @param rq to generate the campaign.
     * @return {@link Optional} of {@link CampaignRsDto}.
     */
    @Override
    public Optional<CampaignRsDto> compute(final CampaignRqDto rq) {
        validateComputeCampaign(rq);
        final UUID userId = getUserId();
        final Map<String, Double> emotions = getEmotionsById(rq.getEmotionalId());
        final BrandClientRsDto brand = getBrandById(rq.getBrandId());
        return generateCampaign(rq, brand, emotions, userId);
    }

    /**
     * get brand by id.
     * @param brandId brand identifier.
     * @return {@link BrandClientRsDto}.
     */
    private BrandClientRsDto getBrandById(final UUID brandId) {
        return brandClient.getById(brandId).map(brand -> {
            log.info("[getBrandById]: brand getting ok: {}", brand.name());
            return brand;
        }).orElseGet(() -> {
            log.error("[getBrandById]: error getting brand.");
            throw InternalServerException.builder()
                    .endpoint("/campaign/compute/")
                    .message("error getting brand, try again.")
                    .build();
        });
    }

    /**
     * getting emotions by id.
     * @param emotionalId emotions identifier.
     * @return {@link Map}.
     */
    private Map<String, Double> getEmotionsById(final UUID emotionalId) {
        return emotionalClient.getById(emotionalId).map(emotions -> {
            log.info("[getEmotionsById]: emotions getting ok: {}", emotions.getEmotions());
            return emotions.getEmotions();
        }).orElseGet(() -> {
            log.error("[getEmotionsById]: error getting emotions.");
            throw InternalServerException.builder()
                    .endpoint("/campaign/compute/")
                    .message("error getting emotions, try again.")
                    .build();
        });
    }

    /**
     * get the user id associated to the campaign.
     * @return {@link UUID} as a user identifier.
     */
    private UUID getUserId() {
        return UUID.randomUUID();
    }

    /**
     * validate the campaign request to generate the campaign.
     * @param rq to validate.
     */
    private void validateComputeCampaign(final CampaignRqDto rq) {
        BasicValidator.isValidField(BasicValidator.isValidString(rq.getKeyphrase()), "keyphrase");
        BasicValidator.isValidField(BasicValidator.isValidId(rq.getBrandId()), "brandId");
        BasicValidator.isValidField(BasicValidator.isValidId(rq.getEmotionalId()), "emotionalId");
        log.info("[validateComputeCampaign]: the data is valid to generate the campaign.");
    }

    private Optional<CampaignRsDto> generateCampaign(
            final CampaignRqDto rq,
            final BrandClientRsDto brand,
            final Map<String, Double> emotions,
            final UUID userId) {
        log.info("[generateCampaign]: ready to generate campaign for userId: {}, brandId: {}", userId, brand.id());
        final long start = BasicValidator.getNow();
        return openAIService.generateCampaign(brand, emotions, rq.getKeyphrase()).flatMap(chat ->
            mapAndSave(chat, BasicValidator.getDuration(start)).flatMap(CampaignMapper::getRsFromDao)
        );
    }

    public Optional<CampaignDao> mapAndSave(final ChatCompletion chat, final Long duration) {
        return CampaignMapper.fromChatGetDao(chat, duration)
                .flatMap(this::save);
    }

    /**
     * save a campaign.
     * @param dao campaign dao to save.
     * @return {@link Optional} of {@link CampaignDao}.
     */
    @Override
    public Optional<CampaignDao> save(final CampaignDao dao) {
        Optional<CampaignDao> result = Optional.empty();

        try {
            campaignRepo.save(dao);
            log.info("[save]: save campaign ok.");
            result = Optional.of(dao);
        } catch (Exception e) {
            log.error("[save]: error saving campaign: {}", e.getMessage());
        }

        return result;
    }
}
