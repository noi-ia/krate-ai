package com.co.solia.emotional.emotional.services.impl;

import com.co.solia.emotional.emotional.models.daos.EmotionalEstimationDao;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRqDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRsDto;
import com.co.solia.emotional.emotional.models.exceptions.InternalServerException;
import com.co.solia.emotional.emotional.models.mappers.EmotionalEstimationMapper;
import com.co.solia.emotional.emotional.models.repos.EmotionalEstimationRepos;
import com.co.solia.emotional.emotional.services.services.EmotionalService;
import com.co.solia.emotional.emotional.services.services.OpenAIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link EmotionalService}.
 *
 * @author luis.bolivar
 */
@Service
@Slf4j
@AllArgsConstructor
public class EmotionalServiceImpl implements EmotionalService {

    /**
     * use openai service to do emotional estimation.
     */
    private OpenAIService openAIService;

    /**
     * dependency on {@link EmotionalEstimationRepos} for use db.
     */
    private EmotionalEstimationRepos emotionalEstimationRepos;


    /**
     * {@inheritDoc}
     * @param emotionalMessage message to process
     * @return
     */
    @Override
    public Optional<EmotionalMessageRsDto> estimate(final EmotionalMessageRqDto emotionalMessage) {
        final UUID userId = UUID.randomUUID();
        final Instant start = Instant.now();
        return openAIService.emotionalEstimation(emotionalMessage.getMessage()).map(resultEE -> {
            final long duration = getDuration(start.toEpochMilli(), Instant.now().toEpochMilli());
            final UUID idEE = UUID.randomUUID();
            return mapAndSaveEE(emotionalMessage.getMessage(), resultEE, userId, idEE, duration)
                    .flatMap(EmotionalEstimationMapper::fromDaoToRsDto);
        }).orElseThrow(() -> InternalServerException.builder().message("Could not call to openai.").endpoint("/emotional/").build());
    }

    /**
     * get duration of openai processing.
     * @param start date.
     * @param end date.
     * @return {@link Timestamp} total duration of openai processing.
     */
    private static long getDuration(long start, long end) {
        return end - start;
    }

    /**
     * get and save the {@link EmotionalEstimationDao}.
     * @param message from request.
     * @param resultEE from openai processing.
     * @param userId user identifier.
     * @param idEE emotional estimation id.
     * @param duration of openai processing.
     * @return {@link EmotionalEstimationDao}.
     */
    private Optional<EmotionalEstimationDao> mapAndSaveEE(
            final String message,
            final ChatCompletion resultEE,
            final UUID userId,
            final UUID idEE,
            final long duration) {
        return EmotionalEstimationMapper
                .fromChatCompletionToDao(message, resultEE, userId, duration, null, idEE)
                .map(result -> {
                    save(result);
                    return result;
                });
    }

    /**
     * save the estimations. {@inheritDoc}
     * @param emotionalEstimation to save
     */
    @Override
    public void save(EmotionalEstimationDao emotionalEstimation) {
        try {
            emotionalEstimationRepos.save(emotionalEstimation);
            log.info("[save]: save estimations ok.");
        } catch (Exception e) {
            log.error("[save]: error saving the estimations: {}", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param id to get emotional estimation.
     * @return
     */
    @Override
    public Optional<EmotionalMessageRsDto> getById(final UUID id) {
        Optional<EmotionalMessageRsDto> result = Optional.empty();

        try {
            result = emotionalEstimationRepos.findById(id)
                    .flatMap(EmotionalEstimationMapper::fromDaoToRsDto);
        } catch (Exception e) {
            log.error("[getById]: Error getting emotional estimation by id: {}, {}", id, e.getMessage());
        }
        return result;
    }

}
