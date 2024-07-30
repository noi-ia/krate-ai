package com.co.solia.emotional.clean.services.impl;

import com.co.solia.emotional.clean.models.daos.CleanBatchDao;
import com.co.solia.emotional.clean.models.daos.CleanDao;
import com.co.solia.emotional.clean.models.dtos.rq.CleanBatchRqDto;
import com.co.solia.emotional.clean.models.dtos.rq.CleanRqDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanBatchRsDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanRsDto;
import com.co.solia.emotional.clean.models.mappers.CleanMapper;
import com.co.solia.emotional.clean.models.repos.CleanBatchRepo;
import com.co.solia.emotional.clean.models.repos.CleanRepo;
import com.co.solia.emotional.clean.services.services.CleanService;
import com.co.solia.emotional.share.utils.validators.Validator;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.services.services.OpenAIService;
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
 * implementation of {@link CleanService}.
 *
 * @author luis.bolivar.
 */
@Service
@Slf4j
@AllArgsConstructor
public class CleanServiceImpl implements CleanService {

    /**
     * repository to persistence the cleaning process with {@link CleanRepo}.
     */
    private CleanRepo cleanRepo;

    /**
     * repository to persistence the cleaning batch process with {@link CleanBatchRepo}.
     */
    private CleanBatchRepo cleanBatchRepo;

    /**
     * dependency on {@link OpenAIService}.
     */
    private OpenAIService openaiService;

    /**
     * {@inheritDoc}.
     * @param cleanRq with the message to clean.
     * @return
     */
    @Override
    public Optional<CleanRsDto> clean(CleanRqDto cleanRq) {
        final UUID userID = UUID.randomUUID();
        log.info("[clean]: ready to clean: {} messages.", 1);
        return cleanMessage(cleanRq.getMessage(), userID, null);
    }

    /**
     * clean message.
     * @param message to clean.
     * @param userID user identifier.
     * @param idBatch batch identifier.
     * @return {@link Optional} of {@link CleanRsDto}.
     */
    private Optional<CleanRsDto> cleanMessage(final String message,final UUID userID, final UUID idBatch) {
        final UUID id = UUID.randomUUID();
        final long start = Validator.getNow();
        return openaiService.clean(message).flatMap(chat ->
                mapAndSave(message, chat, id, userID, idBatch, getDuration(start))
                        .flatMap(CleanMapper::getRsDtoFromDao));
    }

    /**
     * get duration of cleaning process.
     * @param start time.
     * @return long of duration.
     */
    private static long getDuration(final long start) {
        return Validator.getDuration(start, Instant.now().toEpochMilli());
    }

    /**
     * map and save the cleaning process.
     * @param message cleaned.
     * @param chat result of cleaning.
     * @param id cleaning identifier.
     * @param idUser user identifier.
     * @param idBatch batch identifier.
     * @param duration duration of cleaning process.
     * @return {@link Optional} of {@link CleanDao}.
     */
    private Optional<CleanDao> mapAndSave(
            final String message,
            final ChatCompletion chat,
            final UUID id,
            final UUID idUser,
            final UUID idBatch,
            final long duration) {
        return CleanMapper.toDaoFromChatCompletion(chat, id, idUser, idBatch, duration, message)
                .map(dao -> {
                    save(dao);
                    return Optional.of(dao);
                })
                .orElseGet(() -> {
                            log.error("[mapAndSave]: error mapping the cleaning result.");
                            throw InternalServerException.builder()
                                    .endpoint("clean/compute/")
                                    .message("error mapping the cleaning result.")
                                    .build();
                        });
    }

    /**
     * {@inheritDoc}.
     * @param clean to save.
     */
    @Override
    public void save(final CleanDao clean) {
        try {
            cleanRepo.save(clean);
            log.info("[save]: cleaning processing save ok.");
        } catch (Exception e) {
            log.error("[save]: error saving the cleaning process: {}", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}.
     * @param id identifier of cleaning process.
     * @return
     */
    @Override
    public Optional<CleanRsDto> getById(final UUID id) {
        log.info("[getById]: ready to get the clean process by id: {}", id);
        return findById(id).flatMap(CleanMapper::getRsDtoFromDao);
    }

    /**
     * {@inheritDoc}.
     * @param cleansRq with the messages to clean.
     * @return
     */
    @Override
    public Optional<CleanBatchRsDto> cleanList(final CleanBatchRqDto cleansRq) {
        final UUID userId = UUID.randomUUID();
        final UUID id = UUID.randomUUID();
        final long start = Validator.getNow();
        return getCleanMessages(cleansRq, userId, id)
                .flatMap(list -> mapAndSave(list.size(), id, userId, start)
                        .flatMap(dao -> CleanMapper.getRsFromResults(id, list)));
    }

    /**
     * map and save the batch cleaning process.
     * @param totalMessages processed.
     * @param id batch clean identifier.
     * @param userId user identifier.
     * @param start start time of the process.
     * @return {@link Optional} of {@link CleanBatchDao}.
     */
    private Optional<CleanBatchDao> mapAndSave(int totalMessages, UUID id, UUID userId, long start) {
        return CleanMapper.getDaoFromListRsDto(id, totalMessages, userId, getDuration(start))
                .map(dao -> {
                    save(dao);
                    return Optional.of(dao);
                }).orElseGet(() -> {
                    log.error("[mapAndSave]: error mapping the cleaning batch results.");
                    throw InternalServerException.builder()
                            .endpoint("clean/compute/batch/")
                            .message("error mapping the cleaning batch results.")
                            .build();
                });
    }

    /**
     * {@inheritDoc}.
     * @param clean to save.
     */
    @Override
    public void save(final CleanBatchDao clean) {
        try {
            cleanBatchRepo.save(clean);
            log.info("[save]: batch cleaning save ok: id: {}", clean.getId());
        } catch (Exception e) {
            log.error("[save]: error in batch cleaning save: {}", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}.
     * @param id identifier of cleaning batch process.
     * @return
     */
    @Override
    public Optional<CleanBatchRsDto> getByBatchId(final UUID id) {
        return findByBatchId(id)
                .flatMap(list -> CleanMapper.getRsFromDaos(list)
                        .flatMap(results -> CleanMapper.getRsFromResults(id, results)));
    }

    /**
     * clean the all message.
     * @param cleanListRq message to process.
     * @param userId user identifier.
     * @param id batch identifier.
     * @return {@link Optional} of {@link List} of {@link CleanRsDto}.
     */
    private Optional<List<CleanRsDto>> getCleanMessages(final CleanBatchRqDto cleanListRq, UUID userId, UUID id) {
        final List<CleanRsDto> messages = new ArrayList<>(cleanListRq.getMessages().size());
        cleanListRq.getMessages().parallelStream()
                .forEach(message -> cleanMessage(message, userId, id).map(messages::add));
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages);
    }

    /**
     * find a {@link CleanDao} by id.
     * @param id cleaning process identifier.
     * @return {@link Optional} of {@link CleanDao}.
     */
    private Optional<CleanDao> findById(final UUID id) {
        Optional<CleanDao> result = Optional.empty();

        try {
            result = cleanRepo.findById(id);
            log.info("[findById]: clean process got ok from db by id: {}", id);
        } catch (Exception e) {
            log.error("[findById]: error getting clean process by id: {}, error: {}", id, e.getMessage());
        }

        return result;
    }

    /**
     * get a {@link List} of {@link CleanDao} by id.
     * @param id batch identifier.
     * @return {@link Optional} of {@link List} of {@link CleanDao}.
     */
    private Optional<List<CleanDao>> findByBatchId(final UUID id) {
        Optional<List<CleanDao>> result = Optional.empty();

        try {
            List<CleanDao> cleans = cleanRepo.findAllByIdBatch(id);
            result = cleans.isEmpty() ? Optional.empty() : Optional.of(cleans);
            log.info("[findByBatchId]: get data by batch id from db ok: {}", id);
        } catch (Exception e) {
            log.error("[findByBatchId]: error getting cleaning process by batch id: {}, error: {}", id, e.getMessage());
        }

        return result;
    }
}
