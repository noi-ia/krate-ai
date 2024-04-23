package com.co.solia.emotional.models.mappers;

import com.co.solia.emotional.models.daos.EmotionalEstimationDao;
import com.co.solia.emotional.models.dtos.EmotionalMessageRsDto;
import com.co.solia.emotional.models.dtos.EmotionsDto;
import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * utility class to map a multiple emotional estimations models.
 *
 * @author luis.bolivar
 */
@UtilityClass
@Slf4j
public class EmotionalEstimationMapper {

    /**
     * the current prompt message.
     */
    public static final int PROMPT_TOKEN_SIZE = 384;

    /**
     * map from {@link ChatCompletion} to {@link EmotionalEstimationDao}.
     *
     * @param message  from request.
     * @param resultEE result from openai call.
     * @param userId   user identifier.
     * @param duration duration of openai call.
     * @param idBEE    batch id.
     * @return {@link EmotionalEstimationDao}.
     */
    public static Optional<EmotionalEstimationDao> fromChatCompletionToDao(
            final String message,
            final ChatCompletion resultEE,
            final UUID userId,
            final long duration,
            final UUID idBEE,
            final UUID idEE) {
        return Stream.of(resultEE)
                        .filter(Objects::nonNull)
                                .filter(result -> result.choices() != null)
                                        .filter(result -> !result.choices().isEmpty())
                                                .findFirst()
                                                        .map(result -> buildEEDao(message, userId, duration, idBEE, idEE, result));
    }

    /**
     * build a {@link EmotionalEstimationDao}.
     * @param message the message was processed.
     * @param userId user identifier.
     * @param duration duration of processing.
     * @param idBEE id bash process.
     * @param idEE emotional estimation identifier.
     * @param result from openai.
     * @return {@link EmotionalEstimationDao}.
     */
    private static EmotionalEstimationDao buildEEDao(
            final String message,
            final UUID userId,
            final long duration,
            final UUID idBEE,
            final UUID idEE,
            final ChatCompletion result) {
        return EmotionalEstimationDao.builder()
                .message(message)
                .idEE(idEE)
                .idBEE(idBEE)
                .idUser(userId)
                .openAiId(result.id())
                .fingerPrintOpenai(result.systemFingerprint())
                .tokens(result.usage().promptTokens() - PROMPT_TOKEN_SIZE)
                .estimates(result.choices().getFirst().message().content())
                .duration(duration)
                .build();
    }

    /**
     * from a {@link EmotionalEstimationDao} to {@link EmotionalMessageRsDto}.
     * @param dao from dao.
     * @return {@link EmotionalMessageRsDto}.
     */
    public static Optional<EmotionalMessageRsDto> fromDaoToRsDto(final EmotionalEstimationDao dao) {
        return Stream.of(dao)
                        .filter(Objects::nonNull)
                                .filter(d -> d.getIdEE() != null)
                                        .filter(d -> d.getEstimates() != null)
                .filter(d -> !d.getEstimates().isEmpty())
                .filter(d -> !d.getEstimates().isBlank())
                        .findFirst()
                                .map(d -> EmotionalMessageRsDto.builder()
                                        .eeId(dao.getIdEE())
                                        .emotions(getEmotionsFromDao(dao))
                                        .build());
    }

    /**
     * get emotions from dao
     * @param dao to get emotions.
     * @return {@link EmotionsDto}.
     */
    private static EmotionsDto getEmotionsFromDao(EmotionalEstimationDao dao) {
        EmotionsDto emotions = EmotionsDto.builder().build();
        try {
            emotions = new Gson().fromJson(dao.getEstimates(), EmotionsDto.class);
        } catch (Exception e) {
            log.error("[getEmotionsFromDao]: error parsing data from json: {}", e.getMessage());
        }
        return emotions;
    }
}
