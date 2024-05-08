package com.co.solia.emotional.emotional.models.mappers;

import com.co.solia.emotional.emotional.models.daos.EmotionalDao;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRsDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessagesRsDto;
import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * utility class to map a multiple emotional estimations models.
 *
 * @author luis.bolivar
 */
@UtilityClass
@Slf4j
public class EmotionalMapper {

    /**
     * the current prompt message.
     */
    public static final int PROMPT_TOKEN_SIZE = 384;

    /**
     * map from {@link ChatCompletion} to {@link EmotionalDao}.
     *
     * @param message  from request.
     * @param resultEE result from openai call.
     * @param userId   user identifier.
     * @param duration duration of openai call.
     * @param idBEE    batch id.
     * @return {@link EmotionalDao}.
     */
    public static Optional<EmotionalDao> fromChatCompletionToDao(
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
     * build a {@link EmotionalDao}.
     * @param message the message was processed.
     * @param userId user identifier.
     * @param duration duration of processing.
     * @param idBEE id bash process.
     * @param idEE emotional estimation identifier.
     * @param result from openai.
     * @return {@link EmotionalDao}.
     */
    private static EmotionalDao buildEEDao(
            final String message,
            final UUID userId,
            final long duration,
            final UUID idBEE,
            final UUID idEE,
            final ChatCompletion result) {
        return EmotionalDao.builder()
                .message(message)
                .idEE(idEE)
                .idBatch(idBEE)
                .idUser(userId)
                .openAiId(result.id())
                .fingerPrintOpenai(result.systemFingerprint())
                .tokens(result.usage().promptTokens() - PROMPT_TOKEN_SIZE)
                .estimates(result.choices().getFirst().message().content())
                .duration(duration)
                .build();
    }

    /**
     * from a {@link EmotionalDao} to {@link EmotionalMessageRsDto}.
     * @param dao from dao.
     * @return {@link EmotionalMessageRsDto}.
     */
    public static Optional<EmotionalMessageRsDto> fromDaoToRsDto(final EmotionalDao dao) {
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
                                        .message(dao.getMessage())
                                        .build());
    }

    /**
     * get emotions from dao
     * @param dao to get emotions.
     * @return {@link Map} of {@code key}: {@link String} with {@code value}: {@link Double}.
     */
    private static Map<String, Double> getEmotionsFromDao(EmotionalDao dao) {
        Map<String, Double> emotions = Map.of();
        try {
            emotions = new Gson().fromJson(dao.getEstimates(), Map.class);
        } catch (Exception e) {
            log.error("[getEmotionsFromDao]: error parsing data from json: {}", e.getMessage());
        }
        return emotions;
    }

    /**
     * get {@link EmotionalMessagesRsDto} from emotional results.
     * @param ees emotional estimations.
     * @param idBee batch id of emotional estimations.
     * @return {@link Optional} of {@link EmotionalMessagesRsDto}.
     */
    public static Optional<EmotionalMessagesRsDto> getFromEmotionalResults(final List<EmotionalMessageRsDto> ees, final UUID idBee)  {
        return !ees.isEmpty() && idBee != null?
                Optional.of(EmotionalMessagesRsDto.builder()
                                .results(ees)
                                .idBee(idBee)
                        .build()) : Optional.empty();
    }

    /**
     * get a {@link Optional} of {@link List} of {@link EmotionalMessagesRsDto} from a {@link List} of {@link EmotionalDao}.
     * @param daos to get the {@link List} of {@link EmotionalMessagesRsDto}
     * @return {@link Optional} of {@link List} of {@link EmotionalMessagesRsDto}.
     */
    public static Optional<List<EmotionalMessageRsDto>> fromDaosGetDtos(final List<EmotionalDao> daos) {
        return Stream.of(daos)
                .filter(Objects::nonNull)
                .filter(d -> !d.isEmpty())
                .findFirst()
                .flatMap(EmotionalMapper::fromDaosToDtos);
    }

    /**
     * get a {@link Optional} of {@link List} of {@link EmotionalMessagesRsDto} from a {@link List} of {@link EmotionalDao}.
     * @param daos to get the {@link List} of {@link EmotionalMessagesRsDto}
     * @return {@link Optional} of {@link List} of {@link EmotionalMessagesRsDto}.
     */
    private static Optional<List<EmotionalMessageRsDto>> fromDaosToDtos(final List<EmotionalDao> daos) {
        final List<EmotionalMessageRsDto> result = new ArrayList<>(daos.size());
        daos.parallelStream().forEach(dao -> fromDaoToRsDto(dao).ifPresent(result::add));
        return !result.isEmpty() ? Optional.of(result) : Optional.empty();
    }


}
