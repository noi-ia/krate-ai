package com.co.solia.emotional.emotional.services.impl;

import com.co.solia.emotional.emotional.services.services.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage.Role;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.api.OpenAiApi.FunctionTool;

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

    private final String OPENAI_APIKEY;

    private final String PROMPT_EMOTIONAL;

    private final String PROMPT_EMOTIONAL_UNIQUE;

    private final String OPENAI_MODEL;

    @Autowired
    public OpenaiServiceImpl(
            @Value("${solia.emotional.openai.apikey}") final String openaiApikey,
            @Value("${solia.emotional.openai.prompt.emotional}") final String promptEmotional,
            @Value("${solia.emotional.openai.prompt.emotional.unique}") final String promptEmotionalUnique,
            @Value("${solia.emotional.openai.model}") final String openaiModel){
        this.OPENAI_APIKEY = openaiApikey;
        this.PROMPT_EMOTIONAL = promptEmotional;
        this.PROMPT_EMOTIONAL_UNIQUE = promptEmotionalUnique;
        this.OPENAI_MODEL = openaiModel;
    }


    /**
     * {@inheritDoc}
     * @param message to estimate.
     * @return
     */
    @Override
    public Optional<ChatCompletion> emotionalCompute(final String message) {
        log.info("[emotionalEstimation]: starting emotional compute.");
        return callOpenAiEE(message);
    }

    /**
     * {@inheritDoc}
     * @param messages to process.
     * @return
     */
    @Override
    public Optional<ChatCompletion> emotionalComputeUnique(final List<String> messages) {
        log.info("[emotionalComputeUnique]: starting unique emotional compute: {}", messages.size());
        return callOpenAiEEU(messages);
    }

    /**
     * call to OpenAI api.
     * @param messages to estimate.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    private Optional<ChatCompletion> callOpenAiEEU(final List<String> messages) {
        Optional<ChatCompletion> result = Optional.empty();
        try {
            final ResponseEntity<ChatCompletion> response = getOpenAiInstance()
                    .chatCompletionEntity(getEmotionalUniqueChatRequest(messages.toString()));
            result = mapResult(response);
        } catch (Exception e) {
            log.error("[callOpenAiEEU]: Error getting response from OpenAI: {}", e.getMessage());
        }

        return result;
    }

    /**
     * call to OpenAI api.
     * @param message to estimate.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    private Optional<ChatCompletion> callOpenAiEE(final String message) {
        Optional<ChatCompletion> result = Optional.empty();
        try {
            final ResponseEntity<ChatCompletion> response = getOpenAiInstance()
                    .chatCompletionEntity(getEmotionalChatRequest(message));
            result = mapResult(response);
        } catch (Exception e) {
            log.error("[callOpenAiEE]: Error getting response from OpenAI: {}", e.getMessage());
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
    private ChatCompletionRequest getEmotionalChatRequest(final String message) {
        final String model = getModel();
        final Float temperature = 0.0f;
        final ResponseFormat responseFormat = new ResponseFormat("json_object");
        return getEmotionalChatRequest(getEmotionalMessages(message), model, temperature, responseFormat);
    }

    /**
     * method to create the request to emotional unique compute.
     * @param message from the user to get the emotional estimation.
     * @return {@link ChatCompletionRequest}
     */
    private ChatCompletionRequest getEmotionalUniqueChatRequest(final String message) {
        final String model = getModel();
        final Float temperature = 0.0f;
        final ResponseFormat responseFormat = new ResponseFormat("json_object");
        return getEmotionalChatRequest(getEmotionalUniqueMessages(message), model, temperature, responseFormat);
    }


    /**
     * override of the method to create the request.
     * @param messages message to send the model
     * @param model model to use.
     * @param temperature temperature to use.
     * @param responseFormat format of the response.
     * @return {@link ChatCompletionRequest}
     */
    private static ChatCompletionRequest getEmotionalChatRequest(final List<ChatCompletionMessage> messages,
                                                                 final String model,
                                                                 final Float temperature,
                                                                 final ResponseFormat responseFormat){
        return getEmotionalChatRequest(messages, model,
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
    public static ChatCompletionRequest getEmotionalChatRequest(
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
    private ChatCompletionMessage getSysMessEmotional(){
        return new ChatCompletionMessage(PROMPT_EMOTIONAL, Role.SYSTEM);
    }

    /**
     * method to get the system message in {@link ChatCompletionMessage} format to emotional unique compute.
     * @return {@link ChatCompletionMessage}.
     */
    private ChatCompletionMessage getSysMessEmotionalUnique(){
        return new ChatCompletionMessage(PROMPT_EMOTIONAL_UNIQUE, Role.SYSTEM);
    }

    /**
     * decouple method to get the openai model.
     * @return {@link String} with model name.
     */
    private String getModel(){
        return OPENAI_MODEL;
    }

}
