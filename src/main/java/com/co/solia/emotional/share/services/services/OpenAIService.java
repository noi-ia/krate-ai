package com.co.solia.emotional.share.services.services;

import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;

import java.util.List;
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
    Optional<ChatCompletion> emotionalCompute(final String message);

    /**
     * emotional compute for unique estimation in messages.
     * @param messages to process.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    Optional<ChatCompletion> emotionalComputeUnique(final List<String> messages);

    /**
     * clean service with openai service.
     * @param messages to process.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    Optional<ChatCompletion> clean(List<String> messages);
}
