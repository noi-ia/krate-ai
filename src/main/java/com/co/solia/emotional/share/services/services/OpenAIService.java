package com.co.solia.emotional.share.services.services;

import com.co.solia.emotional.campaign.models.dtos.rs.BrandClientRsDto;
import com.co.solia.emotional.keyphrase.models.dtos.rq.KeyphraseOpenaiRqDto;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * service to map the openai behaviors.
 *
 * @author luis.bolivar
 */

public interface OpenAIService {

    /**
     * emotional compute with open-ai.
     * @param message to compute.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    Optional<ChatCompletion> emotionalCompute(String message);

    /**
     * emotional compute for unique estimation in messages.
     * @param messages to process.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    Optional<ChatCompletion> emotionalComputeUnique(List<String> messages);

    /**
     * clean service with openai service.
     * @param message to process.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    Optional<ChatCompletion> clean(String message);

    /**
     * generate the keyphrases.
     * @param emotion related to generate the keyphrases.
     * @param messages messages where comes from the keyphrases.
     * @param emotions emotions associated to the messages.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    Optional<ChatCompletion> getKeyphrases(String emotion, List<String> messages, Map<String, Double> emotions);

    /**
     * generate the keyphrases.
     * @param keyphraseRq a wrapper for the keyphrases.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    Optional<ChatCompletion> getKeyphrases(KeyphraseOpenaiRqDto keyphraseRq);

    /**
     * generate a campaign
     * @param brand associated to the campaign.
     * @param emotions emotions associated to the campaign.
     * @param keyphrase selected to create the campaign.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    Optional<ChatCompletion> generateCampaign(BrandClientRsDto brand, Map<String, Double> emotions, String keyphrase);
}
