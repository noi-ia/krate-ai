package com.co.solia.emotional.emotional.services.impl;

import com.co.solia.emotional.emotional.models.daos.EmotionalBatchDao;
import com.co.solia.emotional.emotional.models.daos.EmotionalDao;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRqDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRsDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessagesRqDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessagesRsDto;
import com.co.solia.emotional.emotional.models.exceptions.InternalServerException;
import com.co.solia.emotional.emotional.models.exceptions.NotFoundException;
import com.co.solia.emotional.emotional.models.mappers.EmotionalMapper;
import com.co.solia.emotional.emotional.models.repos.EmotionalBatchRepo;
import com.co.solia.emotional.emotional.models.repos.EmotionalRepo;
import com.co.solia.emotional.emotional.services.services.EmotionalService;
import com.co.solia.emotional.emotional.services.services.OpenAIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
     * dependency on {@link EmotionalRepo} for use db.
     */
    private EmotionalRepo emotionalRepo;

    /**
     * dependency on {@link EmotionalBatchRepo} for persistence.
     */
    private EmotionalBatchRepo emotionalBatchRepo;

    /**
     * {@inheritDoc}
     * @param emotionalMessage message to process
     * @return
     */
    @Override
    public Optional<EmotionalMessageRsDto> compute(final EmotionalMessageRqDto emotionalMessage) {
        return estimateMessage(emotionalMessage.getMessage(), UUID.randomUUID(), null);
    }

    /**
     * emotional estimation of a message.
     * @param message to estimate.
     * @param userId user identifier.
     * @param idBee id batch identifier.
     * @return {@link Optional} of {@link EmotionalMessagesRsDto}.
     */
    private Optional<EmotionalMessageRsDto> estimateMessage(
            final String message,
            final UUID userId,
            final UUID idBee) {
        final Instant start = Instant.now();
        return openAIService.emotionalEstimation(message).map(resultEE -> {
            final long duration = getDuration(start.toEpochMilli(), Instant.now().toEpochMilli());
            return mapAndSaveEE(message, resultEE, userId, UUID.randomUUID(), idBee, duration)
                    .flatMap(EmotionalMapper::fromDaoToRsDto);
        }).orElseThrow(() -> InternalServerException.builder().message("Could not call to openai.").endpoint("/emotional/").build());
    }

    /**
     * {@inheritDoc}
     * @param messages messages to process
     * @return
     */
    @Override
    public Optional<EmotionalMessagesRsDto> computeList(final EmotionalMessagesRqDto messages) {
        final UUID idBee = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        return estimateMessageList(messages, userId, idBee)
                .flatMap(ees -> EmotionalMapper.getFromEmotionalResults(ees, idBee));
    }

    /**
     * estimate the messages.
     * @param messages to estimate.
     * @param userId user identifier.
     * @param idBee id batch emotional estimation.
     * @return {@link Optional} of {@link List} of {@link EmotionalMessageRsDto}.
     */
    private Optional<List<EmotionalMessageRsDto>> estimateMessageList(final EmotionalMessagesRqDto messages, final UUID userId, final UUID idBee) {
        final long start = Instant.now().toEpochMilli();
        final List<EmotionalMessageRsDto> ees = computeMessages(messages, userId, idBee);
        final long end = Instant.now().toEpochMilli();
        saveBatch(messages.getMessages().size(), userId, idBee, getDuration(start, end));
        return !ees.isEmpty() ? Optional.of(ees) : Optional.empty();
    }

    /**
     * save the batch data.
     * @param amountMessages total amount of messages to process.
     * @param userId user identifier.
     * @param idBee batch emotional estimation identifier.
     * @param duration of processing.
     */
    private void saveBatch(final int amountMessages, final UUID userId, final UUID idBee, final long duration) {
        save(EmotionalBatchDao.builder()
                .amountMessages(amountMessages)
                .id(idBee)
                .userId(userId)
                .duration(duration)
                .build());
    }

    /**
     * compute the all messages.
     * @param messages to process.
     * @param userId user identifier.
     * @param idBee batch emotional estimation id.
     * @return {@link List} of {@link EmotionalMessageRsDto}.
     */
    private List<EmotionalMessageRsDto> computeMessages(final EmotionalMessagesRqDto messages, final UUID userId, final UUID idBee){
        final List<EmotionalMessageRsDto> ees =  new ArrayList<>(messages.getMessages().size());
        messages.getMessages().parallelStream()
                .forEach(message -> estimateMessage(message, userId, idBee).map(ees::add));
        log.info("[computeMessages] total messages processed: {}", messages.getMessages().size());
        return ees;
    }

    /**
     * get duration of openai processing.
     * @param start date.
     * @param end date.
     * @return {@link Long} total duration of openai processing.
     */
    private static long getDuration(long start, long end) {
        return end - start;
    }

    /**
     * get and save the {@link EmotionalDao}.
     * @param message from request.
     * @param resultEE from openai processing.
     * @param userId user identifier.
     * @param idEE emotional estimation id.
     * @param idBee batch emotional estimation id.
     * @param duration of openai processing.
     * @return {@link EmotionalDao}.
     */
    private Optional<EmotionalDao> mapAndSaveEE(
            final String message,
            final ChatCompletion resultEE,
            final UUID userId,
            final UUID idEE,
            final UUID idBee,
            final long duration) {
        return EmotionalMapper
                .fromChatCompletionToDao(message, resultEE, userId, duration, idBee, idEE)
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
    public void save(EmotionalDao emotionalEstimation) {
        try {
            emotionalRepo.save(emotionalEstimation);
            log.info("[save]: save estimations ok.");
        } catch (Exception e) {
            log.error("[save]: error saving the estimations: {}", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param emotionalBatch to save.
     */
    @Override
    public void save(final EmotionalBatchDao emotionalBatch) {
        try{
            emotionalBatchRepo.save(emotionalBatch);
            log.info("[save]: save estimations batch ok.");
        } catch (Exception e) {
            log.error("[save]: error saving batch: {}", e.getMessage());
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
            result = emotionalRepo.findById(id)
                    .flatMap(EmotionalMapper::fromDaoToRsDto);
        } catch (Exception e) {
            log.error("[getById]: Error getting emotional estimation by id: {}, {}", id, e.getMessage());
        }
        return result;
    }

    /**
     * {@inheritDoc}.
     * @param beeId batch emotional estimation identifier.
     * @return
     */
    @Override
    public Optional<EmotionalMessagesRsDto> getECByBatchId(UUID beeId) {
        return getEEByBEE(beeId).map(list ->
            EmotionalMessagesRsDto.builder()
                    .idBee(beeId)
                    .results(EmotionalMapper.fromDaosGetDtos(list)
                            .orElseThrow(() -> NotFoundException.builder()
                                    .endpoint("emotional/{idBEE}")
                                    .message("Data related to idBEE not found.")
                                    .build()))
                    .build());
    }

    /**
     * get from db the emotional estimations.
     * @param beeId to get the emotional estimations.
     * @return {@link Optional} of {@link List} of {@link EmotionalDao}.
     */
    private Optional<List<EmotionalDao>> getEEByBEE(final UUID beeId) {
        Optional<List<EmotionalDao>> result = Optional.empty();
        try {
            List<EmotionalDao> daos = emotionalRepo.findAllByIdBatch(beeId);
            result = Optional.of(daos);
            log.info("[getEEByBEE]: get all emotional compute by batch id: {}, total: {}", beeId, daos.size());
        } catch (Exception e) {
            log.error("[getEEByBEE]: Error getting all emotional computes by batch id: {} error: {}", beeId, e.getMessage());
        }

        return result;
    }

}
