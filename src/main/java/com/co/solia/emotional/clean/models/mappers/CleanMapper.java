package com.co.solia.emotional.clean.models.mappers;

import com.co.solia.emotional.clean.models.daos.CleanBatchDao;
import com.co.solia.emotional.clean.models.daos.CleanDao;
import com.co.solia.emotional.clean.models.dtos.rs.CleanBatchRsDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanRsDto;
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
 * Utility class to transform different entities of clean.
 *
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class CleanMapper {

    /**
     * get a {@link CleanDao} from {@link ChatCompletion}.
     *
     * @param chat     to get the dao.
     * @param id       of cleaning process.
     * @param idUser   user identifier.
     * @param idBatch  batch identifier.
     * @param duration duration of cleaning.
     * @param message  to clean.
     * @return {@link Optional} of {@link CleanDao}.
     */
    public static Optional<CleanDao> toDaoFromChatCompletion(
            final ChatCompletion chat,
            final UUID id,
            final UUID idUser,
            final UUID idBatch,
            final long duration,
            final String message) {
        return Stream.of(chat)
                .filter(Objects::nonNull)
                .filter(c -> !c.choices().isEmpty())
                .findFirst()
                .map(c -> toDaoFromCC(c, id, idUser, idBatch, duration, message));
    }

    /**
     * get a {@link CleanDao} from {@link ChatCompletion}.
     *
     * @param chat     to get the dao.
     * @param id       of cleaning process.
     * @param idUser   user identifier.
     * @param idBatch  batch identifier.
     * @param duration duration of cleaning.
     * @param message  to clean.
     * @return {@link CleanDao}.
     */
    private static CleanDao toDaoFromCC(final ChatCompletion chat,
                                        final UUID id,
                                        final UUID idUser,
                                        final UUID idBatch,
                                        final long duration,
                                        final String message) {
        return CleanDao.builder()
                .id(id)
                .idUser(idUser)
                .idBatch(idBatch)
                .message(message)
                .result(chat.choices().get(0).message().content())
                .tokens(chat.usage().totalTokens() - chat.usage().promptTokens())
                .duration(duration)
                .openAiId(chat.id())
                .fingerPrintOpenai(chat.systemFingerprint())
                .build();
    }

    /**
     * get the {@link CleanRsDto} from the {@link CleanDao}.
     * @param dao to get the {@link CleanRsDto}.
     * @return {@link CleanRsDto}.
     */
    public static Optional<CleanRsDto> getRsDtoFromDao(final CleanDao dao) {
        return Optional.of(CleanRsDto.builder().id(dao.getId()).result(getResult(dao)).build());
    }

    /**
     * get the result structure from a {@link CleanDao}.
     * @param dao to get the result.
     * @return {@link Map} of {@code  key}: {@link String}, {@code  value}: {@link String}
     */
    private static Map<String, String> getResult(final CleanDao dao) {
        return Map.of(
                "message", dao.getMessage(),
                "result", dao.getResult()
        );
    }

    /**
     * get a {@link CleanBatchDao} from messages processed.
     * @param id of batch processing.
     * @param totalMessages cleaned.
     * @param userId user identifier.
     * @param duration duration of process.
     * @return {@link Optional} of {@link CleanBatchDao}.
     */
    public static Optional<CleanBatchDao> getDaoFromListRsDto(
            final UUID id,
            final int totalMessages,
            final UUID userId,
            final long duration) {
        return Optional.of(CleanBatchDao.builder()
                        .id(id)
                        .amountMessages(totalMessages)
                        .userId(userId)
                        .duration(duration)
                .build());
    }

    /**
     * get result by cleaning processed list.
     * @param id of batch processing.
     * @param results clean messages processed.
     * @return {@link Optional} of {@link CleanBatchDao}.
     */
    public static Optional<CleanBatchRsDto> getRsFromResults(final UUID id, final List<CleanRsDto> results) {
        final List<Map<String, String>> cleanMessages = results.parallelStream().map(CleanRsDto::getResult).toList();
        return Optional.of(CleanBatchRsDto.builder()
                .id(id)
                .results(cleanMessages)
                .build());
    }

    /**
     * get a {@link List} of {@link CleanRsDto} from a {@link List} of {@link CleanDao}.
     * @param daos to get the {@link List} of {@link CleanRsDto}.
     * @return {@link List} of {@link CleanRsDto}.
     */
    public static Optional<List<CleanRsDto>> getRsFromDaos(final List<CleanDao> daos) {
        final List<CleanRsDto> results = new ArrayList<>(daos.size());
        daos.parallelStream().forEach(dao -> CleanMapper.getRsDtoFromDao(dao).map(results::add));
        return results.isEmpty() ? Optional.empty() : Optional.of(results);
    }
}
