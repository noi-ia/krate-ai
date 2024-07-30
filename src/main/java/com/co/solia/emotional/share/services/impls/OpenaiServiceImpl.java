package com.co.solia.emotional.share.services.impls;

import com.co.solia.emotional.campaign.models.dtos.rq.CampaignOpenaiRqDto;
import com.co.solia.emotional.keyphrase.models.dtos.rq.KeyphraseOpenaiRqDto;
import com.co.solia.emotional.keyphrase.models.enums.EmotionEnum;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.utils.validators.Validator;
import com.co.solia.emotional.share.services.services.OpenAIService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage.Role;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.api.OpenAiApi.FunctionTool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * implementation of {@link OpenAIService}.
 *
 * @author luis.bolivar
 */
@Slf4j
@Service
public class OpenaiServiceImpl implements OpenAIService {

    /**
     * openai apikey as a secret.
     */
    private final String OPENAI_APIKEY;

    /**
     * openai model used.
     */
    private final String OPENAI_MODEL;

    /**
     * json format return from openai.
     */
    private static final String JSOM_FORMAT_OPENAI = "json_object";

    /**
     * prompt to do campaign.
     */
    private final Resource promptCampaign;

    /**
     * prompt to do emotional_estimation.
     */
    private final Resource promptEmotional;

    /**
     * prompt to do emotional estimation in multiple message.
     */
    private final Resource promptEmotionalUnique;

    /**
     * prompt to clean the comment.
     */
    private final Resource promptClean;

    /**
     * prompt to get the keyphrases.
     */
    private final Resource promptKeyphrase;

    /**
     * the basic constructor for get all required parameters.
     * @param openaiApikey openai api key.
     * @param openaiModel openai model.
     * @param promptKeyphrase prompt for keyphrases.
     * @param promptCampaign prompt for campaign.
     * @param promptEmotional prompt for emotional estimation.
     * @param promptEmotionalUnique prompt for emotional unique estimation.
     * @param promptClean prompt to clean.
     */
    @Autowired
    public OpenaiServiceImpl(
            @Value("${solia.emotional.openai.apikey}") final String openaiApikey,
            @Value("${solia.emotional.openai.model}") final String openaiModel,
            @Value("classpath:templates/prompt_keyphrase.st") final Resource promptKeyphrase,
            @Value("classpath:templates/prompt_campaign.st") final Resource promptCampaign,
            @Value("classpath:templates/prompt_emotional.st") final Resource promptEmotional,
            @Value("classpath:templates/prompt_emotional_unique.st") final Resource promptEmotionalUnique,
            @Value("classpath:templates/prompt_clean.st") final Resource promptClean){
        this.OPENAI_APIKEY = openaiApikey;
        this.OPENAI_MODEL = openaiModel;
        this.promptClean = promptClean;
        this.promptKeyphrase = promptKeyphrase;
        this.promptCampaign = promptCampaign;
        this.promptEmotional = promptEmotional;
        this.promptEmotionalUnique = promptEmotionalUnique;
    }

    /**
     * {@inheritDoc}
     * @param message to estimate.
     * @return
     */
    @Override
    public Optional<ChatCompletion> emotionalCompute(final String message) {
        log.info("[emotionalEstimation]: starting emotional compute.");
        return callEE(message);
    }

    /**
     * {@inheritDoc}
     * @param messages to process.
     * @return
     */
    @Override
    public Optional<ChatCompletion> emotionalComputeUnique(final List<String> messages) {
        log.info("[emotionalComputeUnique]: starting unique emotional compute: {}", messages.size());
        return callEEU(messages);
    }

    /**
     * {@inheritDoc}
     * @param messages to process.
     * @return
     */
    @Override
    public Optional<ChatCompletion> clean(final String messages) {
        log.info("[clean]: starting clean message: {}", messages);
        return callClean(messages);
    }

    /**
     * {@inheritDoc}.
     * @param emotion related to generate the keyphrases.
     * @param messages messages where comes from the keyphrases.
     * @param emotions emotions associated to the messages.
     * @return
     */
    @Override
    public Optional<ChatCompletion> getKeyphrases(
            final String emotion, final List<String> messages, final Map<String, Double> emotions) {
        final String jsonToSend = new Gson().toJson(KeyphraseOpenaiRqDto.builder()
                        .messages(messages)
                        .emotions(emotions)
                        .emotion(EmotionEnum.valueOf(emotion))
                .build());
        return callKeyphrase(jsonToSend);
    }

    /**
     * {@inheritDoc}.
     * @param keyphraseRq a wrapper for the keyphrases.
     * @return
     */
    @Override
    public Optional<ChatCompletion> getKeyphrases(final KeyphraseOpenaiRqDto keyphraseRq) {
        return getKeyphrases(keyphraseRq.getEmotion().toString(), keyphraseRq.getMessages(), keyphraseRq.getEmotions());
    }

    /**
     * generate a campaign
     * @param rq a wrapper for generate the campaign from openai.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    @Override
    public Optional<ChatCompletion> getCampaign(final CampaignOpenaiRqDto rq) {
        log.info("[generateCampaign]: starting campaign compute.");
        return callCampaign(rq);
    }

    /**
     * call to OpenAI api.
     * @param rq data for generate the campaign.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    private Optional<ChatCompletion> callCampaign(final CampaignOpenaiRqDto rq) {
        Optional<ChatCompletion> result = Optional.empty();
        try {
            final ResponseEntity<ChatCompletion> response = getOpenAiInstance()
                    .chatCompletionEntity(getCampaignChatRq(new Gson().toJson(rq)));
            result = mapResult(response);
        } catch (Exception e) {
            log.error("[callCampaign]: Error getting response from OpenAI: {}", e.getMessage());
        }

        return result;
    }

    /**
     * call to OpenAI api.
     * @param messages to estimate.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    private Optional<ChatCompletion> callEEU(final List<String> messages) {
        Optional<ChatCompletion> result = Optional.empty();
        try {
            final ResponseEntity<ChatCompletion> response = getOpenAiInstance()
                    .chatCompletionEntity(getEmotionalUniqueChatRq(messages.toString()));
            result = mapResult(response);
        } catch (Exception e) {
            log.error("[callEEU]: Error getting response from OpenAI: {}", e.getMessage());
        }

        return result;
    }

    /**
     * call to OpenAI api.
     * @param message to estimate.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    private Optional<ChatCompletion> callClean(final String message) {
        Optional<ChatCompletion> result = Optional.empty();
        try {
            final ResponseEntity<ChatCompletion> response = getOpenAiInstance()
                    .chatCompletionEntity(getCleanChatRequest(message));
            result = mapResult(response);
        } catch (Exception e) {
            log.error("[callClean]: Error getting response from OpenAI: {}", e.getMessage());
        }

        return result;
    }

    /**
     * call to OpenAI api.
     * @param message to estimate the keyphrase.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    private Optional<ChatCompletion> callKeyphrase(final String message) {
        Optional<ChatCompletion> result = Optional.empty();
        try {
            final ResponseEntity<ChatCompletion> response = getOpenAiInstance()
                    .chatCompletionEntity(getKeyphraseChatRequest(message));
            result = mapResult(response);
        } catch (Exception e) {
            log.error("[callKeyphrase]: Error getting response from OpenAI: {}", e.getMessage());
        }

        return result;
    }

    /**
     * call to OpenAI api.
     * @param message to estimate.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    private Optional<ChatCompletion> callEE(final String message) {
        Optional<ChatCompletion> result = Optional.empty();
        try {
            final ResponseEntity<ChatCompletion> response = getOpenAiInstance()
                    .chatCompletionEntity(getChatRequest(message));
            result = mapResult(response);
        } catch (Exception e) {
            log.error("[callEE]: Error getting response from OpenAI: {}", e.getMessage());
        }

        return result;
    }

    /**
     * get the OpenApi instance.
     * @return {@link OpenAiApi}
     */
    private OpenAiApi getOpenAiInstance() {
        return new OpenAiApi(OPENAI_APIKEY);
    }

    /**
     * filter the response to validate if was successful.
     * @param response to validate
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    private static Optional<ChatCompletion> mapResult(final ResponseEntity<ChatCompletion> response) {
        return Stream.of(response)
                .filter(Objects::nonNull)
                .filter(rs -> rs.getStatusCode().is2xxSuccessful())
                .filter(rs -> rs.getBody() != null)
                .map(HttpEntity::getBody)
                .findFirst();
    }

    /**
     * method to create the request to emotional-compute.
     * @param message from the user to get the emotional compute.
     * @return {@link ChatCompletionRequest}
     */
    private ChatCompletionRequest getChatRequest(final String message) {
        final String model = getModel();
        final Float temperature = 0.0f;
        final ResponseFormat responseFormat = new ResponseFormat(JSOM_FORMAT_OPENAI);
        return getChatRequest(getEmotionalMessages(message), model, temperature, responseFormat);
    }

    /**
     * method to create the request to emotional unique compute.
     * @param message from the user to get the emotional estimation.
     * @return {@link ChatCompletionRequest}
     */
    private ChatCompletionRequest getEmotionalUniqueChatRq(final String message) {
        final String model = getModel();
        final Float temperature = 0.0f;
        final ResponseFormat responseFormat = new ResponseFormat(JSOM_FORMAT_OPENAI);
        return getChatRequest(getEmotionalUniqueMessages(message), model, temperature, responseFormat);
    }

    /**
     * method to create the request to emotional unique compute.
     * @param message from the user to get the emotional estimation.
     * @return {@link ChatCompletionRequest}
     */
    private ChatCompletionRequest getCampaignChatRq(final String message) {
        final String model = getModel();
        final Float temperature = 0.0f;
        final ResponseFormat format = new ResponseFormat(JSOM_FORMAT_OPENAI);
        return getChatRequest(getCampaignMessages(message), model, temperature, format);
    }


    /**
     * method to create the request to clean compute.
     * @param message from the user to get clean comment.
     * @return {@link ChatCompletionRequest}
     */
    private ChatCompletionRequest getCleanChatRequest(final String message) {
        final String model = getModel();
        final Float temperature = 0.0f;
        return getChatRequest(getCleanMessages(message), model, temperature, null);
    }

    /**
     * method to create the request to clean compute.
     * @param message from the user to get clean comment.
     * @return {@link ChatCompletionRequest}
     */
    private ChatCompletionRequest getKeyphraseChatRequest(final String message) {
        final String model = getModel();
        final Float temperature = 0.0f;
        final ResponseFormat responseFormat = new ResponseFormat(JSOM_FORMAT_OPENAI);
        return getChatRequest(getKeyphraseMessages(message), model, temperature, responseFormat);
    }

    /**
     * override of the method to create the request.
     * @param messages message to send the model
     * @param model model to use.
     * @param temperature temperature to use.
     * @param responseFormat format of the response.
     * @return {@link ChatCompletionRequest}
     */
    private static ChatCompletionRequest getChatRequest(final List<ChatCompletionMessage> messages,
                                                        final String model,
                                                        final Float temperature,
                                                        final ResponseFormat responseFormat){
        return getChatRequest(messages, model,
                null, null, null, 1, null, responseFormat,
                null, null, Boolean.FALSE, temperature, null, null, null, null);
    }

    /**
     * the general method to create a request.
     * @param messages messages to consume in the api.
     * @param model model to use.
     * @param frequencyPenalty penalty rate.
     * @param logItBias maps.
     * @param maxTokens max token for answer.
     * @param n total results.
     * @param presencePenalty rate in presence of penalty.
     * @param responseFormat format of response
     * @param seed integer seed
     * @param stop stop string list.
     * @param stream is stream.
     * @param temperature rate of liberty of the model.
     * @param topP top p.
     * @param tools tools of openai.
     * @param toolChoice tool selected.
     * @param user to map a user.
     * @return {@link ChatCompletionRequest}.
     */
    public static ChatCompletionRequest getChatRequest(
            final List<ChatCompletionMessage> messages,
            final String model,
            final Float frequencyPenalty,
            final Map<String, Integer> logItBias,
            final Integer maxTokens,
            final Integer n,
            final Float presencePenalty,
            final ResponseFormat responseFormat,
            final Integer seed,
            final List<String> stop,
            final Boolean stream,
            final Float temperature,
            final Float topP,
            final List<FunctionTool> tools,
            final String toolChoice,
            final String user) {
        return new ChatCompletionRequest(
                messages,
                model,
                frequencyPenalty,
                logItBias,
                maxTokens,
                n,
                presencePenalty,
                responseFormat,
                seed,
                stop,
                stream,
                temperature,
                topP,
                tools,
                toolChoice,
                user);
    }

    /**
     * method to get the message to send to openai.
     * @param message to create as user message.
     * @return {@link List} of {@link ChatCompletionMessage}.
     */
    private List<ChatCompletionMessage> getEmotionalMessages(final String message){
        List<ChatCompletionMessage> messages = new ArrayList<>();
        try {
            messages = List.of(getSysMessEmotional(), getUserMessage(message));
        } catch (Exception e) {
            log.error("[getEmotionalMessages]: Error getting messages to call open ai: {}, {}", message, e.getMessage());
        }

        return messages;
    }

    /**
     * method to get the message from emotional unique to send to openai.
     * @param message to create as user message.
     * @return {@link List} of {@link ChatCompletionMessage}.
     */
    private List<ChatCompletionMessage> getEmotionalUniqueMessages(final String message){
        List<ChatCompletionMessage> messages = new ArrayList<>();
        try {
            messages = List.of(getSysMessEmotionalUnique(), getUserMessage(message));
        } catch (Exception e) {
            log.error("[getEmotionalUniqueMessages]: Error getting messages to call open ai: {}, {}", message, e.getMessage());
        }

        return messages;
    }

    /**
     * method to get the message from campaign to send to openai.
     * @param message to create as user message for campaign.
     * @return {@link List} of {@link ChatCompletionMessage}.
     */
    private List<ChatCompletionMessage> getCampaignMessages(final String message){
        List<ChatCompletionMessage> messages = new ArrayList<>();
        try {
            messages = List.of(getSysMessCampaign(), getUserMessage(message));
        } catch (Exception e) {
            log.error("[getCampaignMessages]: Error getting messages to call open ai: {}, {}", message, e.getMessage());
        }

        return messages;
    }

    /**
     * method to get the message from clean to send to openai.
     * @param message to create to get the user message.
     * @return {@link List} of {@link ChatCompletionMessage}.
     */
    private List<ChatCompletionMessage> getCleanMessages(final String message){
        List<ChatCompletionMessage> messages = List.of();
        try {
            messages = List.of(getSysMessClean(), getUserMessage(message));
        } catch (Exception e) {
            log.error("[getCleanMessages]: Error getting messages to call open ai: {}, {}", message, e.getMessage());
        }
        return messages;
    }

    /**
     * method to get the message from clean to send to openai.
     * @param message to create to get the user message.
     * @return {@link List} of {@link ChatCompletionMessage}.
     */
    private List<ChatCompletionMessage> getKeyphraseMessages(final String message){
        List<ChatCompletionMessage> messages = new ArrayList<>();
        try {
            messages = List.of(getSysMessKeyphrase(), getUserMessage(message));
        } catch (Exception e) {
            log.error("[getKeyphraseMessages]: Error getting messages to call open ai: {}, {}", message, e.getMessage());
        }
        return messages;
    }

    /**
     * method to get the user message in {@link ChatCompletionMessage} format.
     * @param message to pass to {@link ChatCompletionMessage}.
     * @return {@link ChatCompletionMessage}.
     */
    private static ChatCompletionMessage getUserMessage(final String message) {
        return new ChatCompletionMessage(message, Role.USER);
    }

    /**
     * method to get the system message in {@link ChatCompletionMessage} format to emotional compute.
     * @return {@link ChatCompletionMessage}.
     */
    private ChatCompletionMessage getSysMessEmotional() {
        return getPrompt(promptEmotional)
                .map(campaign -> new ChatCompletionMessage(campaign, Role.SYSTEM))
                .orElseGet(() -> {
                    log.error("[getSysMessEmotional]: error getting system message for emotional estimation.");
                    throw InternalServerException.builder()
                            .message("error getting system message for emotional estimation.")
                            .endpoint("/emotional/compute/")
                            .build();
                });
    }

    /**
     * method to get the system message in {@link ChatCompletionMessage} format to emotional unique compute.
     * @return {@link ChatCompletionMessage}.
     */
    private ChatCompletionMessage getSysMessEmotionalUnique(){
        return getPrompt(promptEmotionalUnique)
                .map(campaign -> new ChatCompletionMessage(campaign, Role.SYSTEM))
                .orElseGet(() -> {
                    log.error("[getSysMessEmotionalUnique]: error getting system message for emotional unique estimation.");
                    throw InternalServerException.builder()
                            .message("error getting system message for emotional unique estimation.")
                            .endpoint("/emotional/compute/unique/")
                            .build();
                });
    }

    /**
     * method to get the system message in {@link ChatCompletionMessage} format to campaign compute.
     * @return {@link ChatCompletionMessage}.
     */
    private ChatCompletionMessage getSysMessCampaign() {
        return getPrompt(promptCampaign)
                        .map(campaign -> new ChatCompletionMessage(campaign, Role.SYSTEM))
                        .orElseGet(() -> {
                            log.error("[getSysMessCampaign]: error getting system message for campaign");
                            throw InternalServerException.builder()
                                    .message("error getting system message for campaign")
                                    .endpoint("/campaign/compute/")
                                    .build();
                        });
    }

    private Optional<String> getPrompt(final Resource resource) {
        Optional<String> campaignPrompt = Optional.empty();
        try {
            final String prompt = resource.getContentAsString(StandardCharsets.UTF_8);
            campaignPrompt = Validator.isValidString(prompt) ? Optional.of(prompt) : Optional.empty();
        } catch (IOException e) {
            log.error("[getPrompt]: error getting prompt: {}", e.getMessage());
        }
        return campaignPrompt;
    }

    /**
     * decouple method to get the openai model.
     * @return {@link String} with model name.
     */
    private String getModel(){
        return OPENAI_MODEL;
    }

    /**
     * method to get the system message in {@link ChatCompletionMessage} format to clean.
     * @return {@link ChatCompletionMessage}.
     */
    private ChatCompletionMessage getSysMessClean(){
        return getPrompt(promptClean)
                .map(campaign -> new ChatCompletionMessage(campaign, Role.SYSTEM))
                .orElseGet(() -> {
                    log.error("[getSysMessClean]: error getting system message for clean");
                    throw InternalServerException.builder()
                            .message("error getting system message for clean.")
                            .endpoint("/clean/compute/")
                            .build();
                });
    }

    /**
     * method to get the system message in {@link ChatCompletionMessage} format to clean.
     * @return {@link ChatCompletionMessage}.
     */
    private ChatCompletionMessage getSysMessKeyphrase(){
        return getPrompt(promptKeyphrase)
                .map(campaign -> new ChatCompletionMessage(campaign, Role.SYSTEM))
                .orElseGet(() -> {
                    log.error("[getSysMessKeyphrase]: error getting system message for keyphrases");
                    throw InternalServerException.builder()
                            .message("error getting system message for keyphrases.")
                            .endpoint("/keyphrase/compute/")
                            .build();
                });
    }

}
