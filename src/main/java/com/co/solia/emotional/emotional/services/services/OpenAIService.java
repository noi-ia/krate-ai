package com.co.solia.emotional.emotional.services.services;

import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;

import java.util.Optional;

/**
 * service to map the openai behaviors.
 *
 * @author luis.bolivar
 */

public interface OpenAIService {

    /**
     * emotional estimation with open-ai.
     * @param message to estimate.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    Optional<ChatCompletion> emotionalEstimation(final String message);
}
