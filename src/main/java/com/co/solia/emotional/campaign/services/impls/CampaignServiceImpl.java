package com.co.solia.emotional.campaign.services.impls;

import com.co.solia.emotional.campaign.clients.clients.KeyphraseClient;
import com.co.solia.emotional.campaign.models.daos.CampaignDao;
import com.co.solia.emotional.campaign.models.dtos.rq.CampaignRqDto;
import com.co.solia.emotional.campaign.models.dtos.rs.CampaignRsDto;
import com.co.solia.emotional.campaign.models.dtos.rs.KeyphraseClientRsDto;
import com.co.solia.emotional.campaign.models.repos.CampaignRepo;
import com.co.solia.emotional.campaign.services.services.CampaignService;
import com.co.solia.emotional.share.services.services.OpenAIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     * dependency on {@link KeyphraseClient}.
     */
    private KeyphraseClient keyphraseClient;

    /**
     * compute for generate the campaigns.
     * @param request to generate the campaign.
     * @param emotion to generate the campaigns.
     * @return {@link Optional} of {@link CampaignRsDto}.
     */
    @Override
    public Optional<CampaignRsDto> compute(final CampaignRqDto request, final String emotion) {
        getKeyphrases(request.getMessages(), emotion).map(keyphrases -> {
            log.info("[compute]: keyphrase: " + keyphrases);
            return Optional.empty();
        });
        return Optional.empty();
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
     * get the keyphrases from messages.
     * @param messages to generate the keyphrases.
     * @param emotion associated with the message.
     * @return {@link Optional} of {@link KeyphraseClientRsDto}.
     */
    private Optional<KeyphraseClientRsDto> getKeyphrases(final List<String> messages, final String emotion) {
        log.info("[getKeyphrases]: ready to generate the keyphrases: {} and emotion: {}", messages.size(), emotion);
        return keyphraseClient.generateKeyphrases(messages, emotion);
    }
}
