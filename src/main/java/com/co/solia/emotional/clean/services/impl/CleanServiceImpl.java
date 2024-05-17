package com.co.solia.emotional.clean.services.impl;

import com.co.solia.emotional.clean.models.daos.CleanDao;
import com.co.solia.emotional.clean.models.dtos.rq.CleanRqDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanRsDto;
import com.co.solia.emotional.clean.models.mappers.CleanMapper;
import com.co.solia.emotional.clean.models.respos.CleanRepo;
import com.co.solia.emotional.clean.services.services.CleanService;
import com.co.solia.emotional.emotional.utils.BasicValidator;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.services.services.OpenAIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        final UUID id = UUID.randomUUID();
        final long start = Instant.now().toEpochMilli();
        return openaiService.clean(cleanRq.getMessage()).flatMap(chat ->
                    mapAndSave(cleanRq.getMessage(), chat, id, userID, null, getDuration(start))
                    .flatMap(CleanMapper::getRsDtoFromDao));
    }

    /**
     * get duration of cleaning process.
     * @param start time.
     * @return long of duration.
     */
    private static long getDuration(final long start) {
        return BasicValidator.getDuration(start, Instant.now().toEpochMilli());
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
}
