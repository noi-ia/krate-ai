package com.co.solia.emotional.emotional.services.impl;

import com.co.solia.emotional.emotional.clients.clients.CleanClient;
import com.co.solia.emotional.emotional.models.daos.EmotionalBatchDao;
import com.co.solia.emotional.emotional.models.daos.EmotionalDao;
import com.co.solia.emotional.emotional.models.daos.EmotionalUniqueDao;
import com.co.solia.emotional.emotional.models.dtos.rq.EmotionalRqDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalRsDto;
import com.co.solia.emotional.emotional.models.dtos.rq.EmotionalBatchRqDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalBatchRsDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalUniqueRsDto;
import com.co.solia.emotional.emotional.models.mappers.EmotionalMapper;
import com.co.solia.emotional.emotional.models.repos.EmotionalBatchRepo;
import com.co.solia.emotional.emotional.models.repos.EmotionalRepo;
import com.co.solia.emotional.emotional.models.repos.EmotionalUniqueRepo;
import com.co.solia.emotional.emotional.services.services.EmotionalService;
import com.co.solia.emotional.share.models.validators.Validator;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.models.exceptions.NotFoundException;
import com.co.solia.emotional.share.services.services.OpenAIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * dependency on {@link EmotionalUniqueRepo} for persistence.
     */
    private EmotionalUniqueRepo emotionalUniqueRepo;

    /**
     * dependency on {@link CleanClient}.
     */
    private CleanClient cleanClient;

    /**
     * {@inheritDoc}
     * @param emotionalMessage message to process
     * @return
     */
    @Override
    public Optional<EmotionalRsDto> compute(final EmotionalRqDto emotionalMessage) {
        return estimateMessage(emotionalMessage.getMessage(), UUID.randomUUID(), null);
    }

    /**
     * emotional estimation of a message.
     * @param message to estimate.
     * @param userId user identifier.
     * @param idBee id batch identifier.
     * @return {@link Optional} of {@link EmotionalBatchRsDto}.
     */
    private Optional<EmotionalRsDto> estimateMessage(
            final String message,
            final UUID userId,
            final UUID idBee) {
        final Instant start = Instant.now();
        final String cleanMessage = cleanMessage(message);
        return openAIService.emotionalCompute(cleanMessage).map(resultEE -> {
            final long duration = Validator.getDuration(start.toEpochMilli(), Instant.now().toEpochMilli());
            return mapAndSaveEE(cleanMessage, resultEE, userId, UUID.randomUUID(), idBee, duration)
                    .flatMap(EmotionalMapper::fromDaoToRsDto);
        }).orElseThrow(() -> InternalServerException.builder().message("Could not call to openai.").endpoint("/emotional/").build());
    }

    /**
     * call to clean the message to process.
     * @param message to clean.
     * @return {@link String}.
     */
    private String cleanMessage(final String message) {
        return cleanClient.clean(message).orElseGet(() -> {
                log.error("[cleanMessage] error cleaning message: {}", message);
                return message;
        });
    }

    /**
     * {@inheritDoc}
     * @param messages messages to process
     * @return
     */
    @Override
    public Optional<EmotionalBatchRsDto> computeList(final EmotionalBatchRqDto messages) {
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
     * @return {@link Optional} of {@link List} of {@link EmotionalRsDto}.
     */
    private Optional<List<EmotionalRsDto>> estimateMessageList(final EmotionalBatchRqDto messages, final UUID userId, final UUID idBee) {
        final long start = Instant.now().toEpochMilli();
        final List<EmotionalRsDto> ees = computeMessages(messages, userId, idBee);
        final long end = Instant.now().toEpochMilli();
        saveBatch(messages.getMessages().size(), userId, idBee, Validator.getDuration(start, end));
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
     * @return {@link List} of {@link EmotionalRsDto}.
     */
    private List<EmotionalRsDto> computeMessages(final EmotionalBatchRqDto messages, final UUID userId, final UUID idBee){
        final List<EmotionalRsDto> ees =  new ArrayList<>(messages.getMessages().size());
        messages.getMessages().parallelStream()
                .forEach(message -> estimateMessage(message, userId, idBee).map(ees::add));
        log.info("[computeMessages] total messages processed: {}", messages.getMessages().size());
        return ees;
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
    @Transactional
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
    @Transactional
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
    public Optional<EmotionalRsDto> getById(final UUID id) {
        Optional<EmotionalRsDto> result = Optional.empty();

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
     * @param id batch emotional estimation identifier.
     * @return
     */
    @Override
    public Optional<EmotionalBatchRsDto> getByBatchId(final UUID id) {
        return getEEByBEE(id).map(list ->
            EmotionalBatchRsDto.builder()
                    .id(id)
                    .results(EmotionalMapper.fromDaosGetDtos(list)
                            .orElseThrow(() -> NotFoundException.builder()
                                    .endpoint("emotional/compute/batch/" + id)
                                    .message("Data related to idBEE not found.")
                                    .build()))
                    .build());
    }

    /**
     * {@inheritDoc}.
     * @param emotionalBatch messages to have a unique process.
     * @return
     */
    @Override
    public Optional<EmotionalUniqueRsDto> computeUnique(final EmotionalBatchRqDto emotionalBatch) {
        final UUID id = UUID.randomUUID();
        final UUID userID = UUID.randomUUID();
        final long start = Instant.now().toEpochMilli();
        return openAIService.emotionalComputeUnique(emotionalBatch.getMessages()).map(chat -> {
            mapAndSave(emotionalBatch.getMessages(), chat, id, userID,
                    Validator.getDuration(start, Instant.now().toEpochMilli()));
           return EmotionalUniqueRsDto.builder()
                   .id(id)
                   .emotions(EmotionalMapper.getEmotionsFromChatCompletion(chat))
                   .messages(emotionalBatch.getMessages())
                   .build();
        });
    }

    /**
     * {@inheritDoc}.
     * @param id unique emotional compute identifier.
     * @return
     */
    @Override
    public Optional<EmotionalUniqueRsDto> getByUniqueId(final UUID id) {
        return findById(id).flatMap(EmotionalMapper::getFromDao);
    }

    /**
     * find emotional unique processed by id.
     * @param id to get the emotional unique id.
     * @return {@link Optional} of {@link EmotionalUniqueDao}.
     */
    private Optional<EmotionalUniqueDao> findById(final UUID id) {
        Optional<EmotionalUniqueDao> result = Optional.empty();
        try {
            result = emotionalUniqueRepo.findById(id);
            log.info("[findById]: the db response ok getting emotional unique.");
        } catch(Exception e) {
            log.error("[findById]: Error finding emotional unique by id: {}, error: {}", id, e.getMessage());
        }
        return result;
    }

    /**
     * map and save the emotional unique processing.
     * @param messages to process.
     * @param chat the result of processing.
     * @param id emotional unique identifier.
     * @param userId user identifier.
     * @param duration of processing.
     */
    private void mapAndSave(
            final List<String> messages,
            final ChatCompletion chat,
            final UUID id,
            final UUID userId,
            final long duration) {
        EmotionalMapper.getEUFromChatCompletion(chat, id, userId, messages, duration)
                .ifPresent(this::save);
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

    /**
     * {@inheritDoc}.
     * @param emotionalUnique to save.
     */
    @Transactional
    public void save(final EmotionalUniqueDao emotionalUnique) {
        try {
            emotionalUniqueRepo.save(emotionalUnique);
            log.info("[save]: emotional unique saved ok.");
        } catch (Exception e) {
            log.error("[save]: error saving emotional unique processing: {}", e.getMessage());
        }
    }

}
