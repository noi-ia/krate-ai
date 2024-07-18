package com.co.solia.emotional.campaign.services.impls;

import com.co.solia.emotional.campaign.clients.clients.BrandClient;
import com.co.solia.emotional.campaign.clients.clients.KeyphraseClient;
import com.co.solia.emotional.campaign.models.daos.CampaignDao;
import com.co.solia.emotional.campaign.models.dtos.rq.CampaignOpenaiRqDto;
import com.co.solia.emotional.campaign.models.dtos.rq.CampaignRqDto;
import com.co.solia.emotional.campaign.models.dtos.rs.BrandClientRsDto;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;
import com.co.solia.emotional.campaign.models.mappers.BrandMapper;
import com.co.solia.emotional.campaign.models.mappers.CampaignMapper;
import com.co.solia.emotional.campaign.models.repos.CampaignRepo;
import com.co.solia.emotional.campaign.services.services.CampaignService;
import com.co.solia.emotional.share.clients.clients.EmotionalClient;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.models.validators.Validator;
import com.co.solia.emotional.share.models.validators.ServiceValidator;
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
     * dependency on {@link KeyphraseClient}.
     */
    private KeyphraseClient keyphraseClient;

    /**
     * compute for generate the campaigns.
     * @param rq to generate the campaign.
     * @return {@link Optional} of {@link CampaignRsDto}.
     */
    @Override
    public Optional<CampaignRsDto> compute(final CampaignRqDto rq) {
        ServiceValidator.validateCampaignRq(rq);
        final UUID userId = getUserId();
        final String keyphrase = getKeyphraseById(rq.getKeyphraseId());
        final Map<String, Double> emotions = getEmotionsById(rq.getEmotionalId());
        final BrandClientRsDto brand = getBrandById(rq.getBrandId());
        return generateCampaign(keyphrase, brand, emotions, userId, rq.getEmotionalId());
    }

    /**
     * get the keyphrase by id.
     * @param id to get the keyphrase.
     * @return {@link String} with the keyphrase plain.
     */
    private String getKeyphraseById(final UUID id) {
        return keyphraseClient.getKeyphraseById(id).map(keyphrase -> {
            log.info("[getKeyphraseById]: get keyphrase ok.");
            return keyphrase.keyphrase();
        }).orElseGet(() -> {
           log.error("[getKeyphraseById]: error getting the keyphrase by id: {}.", id);
           throw InternalServerException.builder()
                   .endpoint("/campaign/compute/")
                   .message("error getting the keyphrase by id: " + id)
                   .build();
        });
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

    private Optional<CampaignRsDto> generateCampaign(
            final String keyphrase,
            final BrandClientRsDto brand,
            final Map<String, Double> emotions,
            final UUID userId, final UUID emotionsId) {
        log.info("[generateCampaign]: ready to generate campaign for userId: {}, brandId: {}", userId, brand.id());
        final long start = Validator.getNow();
        final UUID id = UUID.randomUUID();
        return openAIService.getCampaign(getRqCampaignOpenai(brand, emotions, keyphrase)).flatMap(chat ->
            mapAndSave(chat, Validator.getDuration(start), id, keyphrase, brand.id(), userId, emotionsId)
                    .flatMap(CampaignMapper::getRsFromDao)
        );
    }

    /**
     * get the request to call the campaign generator from openai.
     * @param brand brand associated with the campaign,
     * @param emotions emotions associated with the comments.
     * @param keyphrase to generate the campaign,
     * @return {@link CampaignOpenaiRqDto}.
     */
    private CampaignOpenaiRqDto getRqCampaignOpenai(
            final BrandClientRsDto brand,
            final Map<String, Double> emotions,
            final String keyphrase) {
        return CampaignOpenaiRqDto.builder()
                .brand(BrandMapper.getDtoFromRs(brand))
                .emotions(emotions)
                .keyphrase(keyphrase)
                .build();
    }

    public Optional<CampaignDao> mapAndSave(
            final ChatCompletion chat,
            final Long duration,
            final UUID id,
            final String keyphrase,
            final UUID brandId,
            final UUID userId,
            final UUID emotionsId) {
        return CampaignMapper.fromChatGetDao(chat, duration, id, keyphrase, brandId, userId, emotionsId)
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

    /**
     * get campaign by id.
     *
     * @param id to get the campaign.
     * @return {@link Optional} of {@link CampaignRsDto}.
     */
    @Override
    public Optional<CampaignRsDto> getById(final UUID id) {
        log.info("[getById]: ready to get the campaign by id: {}", id);
        return getCampaignById(id).flatMap(CampaignMapper::getRsFromDao);
    }

    /**
     * get a campaign by id.
     * @param id to get the campaign.
     * @return {@link Optional} of {@link CampaignDao}.
     */
    private Optional<CampaignDao> getCampaignById(final UUID id) {
        Optional<CampaignDao> result = Optional.empty();

        try {
            result = campaignRepo.findById(id);
            log.info("[getCampaignById]: get campaign ok.");
        } catch (Exception e) {
            log.error("[getCampaignById]: error getting campaign by id: {}, with error: {}", id, e.getMessage());
        }

        return result;
    }
}
