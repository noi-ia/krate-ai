package com.co.solia.emotional.emotional.models.mappers;

import com.co.solia.emotional.emotional.models.daos.EmotionalDao;
import com.co.solia.emotional.emotional.models.daos.EmotionalUniqueDao;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalRsDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalBatchRsDto;
import com.co.solia.emotional.emotional.models.dtos.rs.EmotionalUniqueRsDto;
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
    public static final int PROMPT_EMOTIONAL_TOKEN_SIZE = 384;

    /**
     * the current prompt message.
     */
    public static final int PROMPT_EMOTIONAL_UNIQUE_TOKEN_SIZE = 471;

    /**
     * map from {@link ChatCompletion} to {@link EmotionalDao}.
     *
     * @param message  from request.
     * @param chat result from openai call.
     * @param userId   user identifier.
     * @param duration duration of openai call.
     * @param idBEE    batch id.
     * @return {@link EmotionalDao}.
     */
    public static Optional<EmotionalDao> fromChatCompletionToDao(
            final String message,
            final ChatCompletion chat,
            final UUID userId,
            final long duration,
            final UUID idBEE,
            final UUID idEE) {
        return Stream.of(chat)
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
                .id(idEE)
                .idBatch(idBEE)
                .idUser(userId)
                .openAiId(result.id())
                .fingerPrintOpenai(result.systemFingerprint())
                .tokens(result.usage().promptTokens() - PROMPT_EMOTIONAL_TOKEN_SIZE)
                .estimates(result.choices().getFirst().message().content())
                .duration(duration)
                .build();
    }

    /**
     * from a {@link EmotionalDao} to {@link EmotionalRsDto}.
     * @param dao from dao.
     * @return {@link EmotionalRsDto}.
     */
    public static Optional<EmotionalRsDto> fromDaoToRsDto(final EmotionalDao dao) {
        return Stream.of(dao)
                        .filter(Objects::nonNull)
                                .filter(d -> d.getId() != null)
                                        .filter(d -> d.getEstimates() != null)
                .filter(d -> !d.getEstimates().isEmpty())
                .filter(d -> !d.getEstimates().isBlank())
                        .findFirst()
                                .map(d -> EmotionalRsDto.builder()
                                        .id(dao.getId())
                                        .emotions(getEmotionsFromDao(dao))
                                        .message(dao.getMessage())
                                        .build());
    }

    /**
     * get emotions from dao
     * @param dao to get emotions.
     * @return {@link Map} of {@code key}: {@link String} with {@code value}: {@link Double}.
     */
    private static Map<String, Double> getEmotionsFromDao(final EmotionalDao dao) {
        Map<String, Double> emotions = Map.of();
        try {
            emotions = new Gson().fromJson(dao.getEstimates(), Map.class);
        } catch (Exception e) {
            log.error("[getEmotionsFromDao]: error parsing data from json: {}", e.getMessage());
        }
        return emotions;
    }

    /**
     * get emotions from dao
     * @param chatCompletion {@link ChatCompletion} to get emotions.
     * @return {@link Map} of {@code key}: {@link String} with {@code value}: {@link Double}.
     */
    public static Map<String, Double> getEmotionsFromChatCompletion(final ChatCompletion chatCompletion) {
        Map<String, Double> emotions = Map.of();
        try {
                emotions = new Gson().fromJson(chatCompletion.choices().get(0).message().content(), Map.class);
        } catch (Exception e) {
            log.error("[getEmotionsFromChatCompletion]: error parsing data from json: {}", e.getMessage());
        }
        return emotions;
    }

    /**
     * get {@link EmotionalBatchRsDto} from emotional results.
     * @param ees emotional estimations.
     * @param idBee batch id of emotional estimations.
     * @return {@link Optional} of {@link EmotionalBatchRsDto}.
     */
    public static Optional<EmotionalBatchRsDto> getFromEmotionalResults(final List<EmotionalRsDto> ees, final UUID idBee)  {
        return !ees.isEmpty() && idBee != null?
                Optional.of(EmotionalBatchRsDto.builder()
                                .results(ees)
                                .id(idBee)
                        .build()) : Optional.empty();
    }

    /**
     * get a {@link Optional} of {@link List} of {@link EmotionalBatchRsDto} from a {@link List} of {@link EmotionalDao}.
     * @param daos to get the {@link List} of {@link EmotionalBatchRsDto}
     * @return {@link Optional} of {@link List} of {@link EmotionalBatchRsDto}.
     */
    public static Optional<List<EmotionalRsDto>> fromDaosGetDtos(final List<EmotionalDao> daos) {
        return Stream.of(daos)
                .filter(Objects::nonNull)
                .filter(d -> !d.isEmpty())
                .findFirst()
                .flatMap(EmotionalMapper::fromDaosToDtos);
    }

    /**
     * get a {@link Optional} of {@link List} of {@link EmotionalBatchRsDto} from a {@link List} of {@link EmotionalDao}.
     * @param daos to get the {@link List} of {@link EmotionalBatchRsDto}
     * @return {@link Optional} of {@link List} of {@link EmotionalBatchRsDto}.
     */
    private static Optional<List<EmotionalRsDto>> fromDaosToDtos(final List<EmotionalDao> daos) {
        final List<EmotionalRsDto> result = new ArrayList<>(daos.size());
        daos.parallelStream().forEach(dao -> fromDaoToRsDto(dao).ifPresent(result::add));
        return !result.isEmpty() ? Optional.of(result) : Optional.empty();
    }

    /**
     * get a {@link Optional} of {@link EmotionalUniqueDao} from a {@link ChatCompletion} and other parameters.
     * @param chat a {@link ChatCompletion} object from {@code openai}.
     * @param id emotional unique identifier.
     * @param userId user identifier.
     * @param messages messages to process.
     * @param duration duration of the processing.
     * @return {@link Optional} of {@link EmotionalUniqueDao}.
     */
    public static Optional<EmotionalUniqueDao> getEUFromChatCompletion(
            final ChatCompletion chat, final UUID id, final UUID userId, final List<String> messages, Long duration) {
        log.info("[getEUFromChatCompletion]: duration: {}, userId: {}, total-messages: {}", duration, userId, messages.size());
        return Stream.of(chat)
                .filter(Objects::nonNull)
                .filter(c -> !c.choices().isEmpty())
                .findFirst()
                .map(c -> getEUFromCC(c, id, userId, messages, duration));
    }

    /**
     * build internally the {@link EmotionalUniqueDao}.
     * @param chat from {@link ChatCompletion}.
     * @param id of unique processing.
     * @param userId user identifier.
     * @param messages list of messages to process.
     * @param duration of estimation.
     * @return {@link EmotionalUniqueDao}.
     */
    private static EmotionalUniqueDao getEUFromCC(
            final ChatCompletion chat, final UUID id, final UUID userId, final List<String> messages, Long duration) {
        return EmotionalUniqueDao.builder()
                .id(id)
                .idUser(userId)
                .messages(messages)
                .fingerPrintOpenai(chat.systemFingerprint())
                .openAiId(chat.id())
                .estimations(chat.choices().get(0).message().content())
                .duration(duration)
                .tokens(chat.usage().promptTokens() - PROMPT_EMOTIONAL_UNIQUE_TOKEN_SIZE)
                .build();
    }

    /**
     * get a {@link EmotionalUniqueRsDto} from a {@link EmotionalUniqueDao}.
     * @param dao to get the {@link EmotionalUniqueRsDto}.
     * @return {@link Optional} with a {@link EmotionalUniqueRsDto} from a {@link EmotionalUniqueDao}.
     */
    public static Optional<EmotionalUniqueRsDto> getFromDao(final EmotionalUniqueDao dao) {
        return Stream.of(dao)
                .filter(Objects::nonNull)
                .findFirst()
                .map(d -> EmotionalUniqueRsDto.builder()
                        .id(d.getId())
                        .messages(d.getMessages())
                        .emotions(getEmotionsFromString(d.getEstimations()))
                        .build());
    }

    /**
     * get emotions from {@link String}.
     * @param results to get emotions.
     * @return {@link Map} of {@code key}: {@link String} with {@code value}: {@link Double}.
     */
    private static Map<String, Double> getEmotionsFromString(final String results) {
        Map<String, Double> emotions = Map.of();
        try {
            emotions = new Gson().fromJson(results, Map.class);
        } catch (Exception e) {
            log.error("[getEmotionsFromString]: error parsing data from json: {}", e.getMessage());
        }
        return emotions;
    }
}
