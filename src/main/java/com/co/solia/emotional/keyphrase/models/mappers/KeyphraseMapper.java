package com.co.solia.emotional.keyphrase.models.mappers;

import com.co.solia.emotional.keyphrase.models.daos.KeyphraseDao;
import com.co.solia.emotional.keyphrase.models.daos.KeyphrasesDao;
import com.co.solia.emotional.keyphrase.models.dtos.rq.EmotionalClientRqDto;
import com.co.solia.emotional.keyphrase.models.dtos.rq.KeyphraseOpenaiRqDto;
import com.co.solia.emotional.keyphrase.models.dtos.rq.KeyphraseRqDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphraseRsDto;
import com.co.solia.emotional.share.models.dtos.rs.EmotionalClientRsDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.EmotionalDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphrasesRsDto;
import com.co.solia.emotional.keyphrase.models.enums.EmotionEnum;
import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * utility class for map around the keyphrase resource.
 *
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class KeyphraseMapper {

    /**
     * get a {@link EmotionalClientRqDto} from a {@link KeyphraseRqDto}.
     * @param keyphraseRq request to map.
     * @return {@link Optional} of {@link EmotionalClientRqDto}.
     */
    public Optional<EmotionalClientRqDto> getFromRqDto(final KeyphraseRqDto keyphraseRq){
        return Stream.of(keyphraseRq)
                .filter(Objects::nonNull)
                .filter(rq -> rq.getMessages() != null)
                .filter(rq -> !rq.getMessages().isEmpty())
                .map(KeyphraseMapper::getEmotionalRqFromRqDto)
                .findFirst();
    }

    /**
     * private get a {@link EmotionalClientRqDto} from a {@link KeyphraseRqDto}.
     * @param keyphraseRq request to map.
     * @return {@link EmotionalClientRqDto}.
     */
    private EmotionalClientRqDto getEmotionalRqFromRqDto(final KeyphraseRqDto keyphraseRq) {
        return EmotionalClientRqDto.builder()
                .messages(keyphraseRq.getMessages())
                .build();
    }

    /**
     * get a {@link KeyphrasesDao} from {@link ChatCompletion} and {@link EmotionalClientRsDto}.
     * @param chat result from openai.
     * @param id of process.
     * @param emotionalRs emotional results.
     * @param duration duration of process.
     * @param userId user identifier.
     * @param emotion emotion to get the keyphrase.
     * @return an {@link Optional} of {@link KeyphrasesDao}.
     */
    public static Optional<KeyphrasesDao> getDaoFromChatCompletion(
            final ChatCompletion chat,
            final UUID id,
            final EmotionalClientRsDto emotionalRs,
            final long duration,
            final UUID userId,
            final String emotion) {
        return Stream.of(chat)
                .filter(Objects::nonNull)
                .filter(c -> !c.choices().isEmpty())
                .findFirst()
                .map(c -> getDaoFromCC(c, id, emotionalRs, duration, userId, emotion));
    }

    /**
     * get a {@link KeyphrasesDao} from {@link ChatCompletion} and {@link EmotionalClientRsDto}.
     * @param chat result from openai.
     * @param id of process.
     * @param emotionalRs emotional results.
     * @param duration duration of process.
     * @param userId user identifier.
     * @param emotion emotion to get the keyphrase.
     * @return a {@link KeyphrasesDao}.
     */
    private static KeyphrasesDao getDaoFromCC(
            final ChatCompletion chat,
            final UUID id,
            final EmotionalClientRsDto emotionalRs,
            final long duration,
            final UUID userId,
            final String emotion) {
        return KeyphrasesDao.builder()
                .id(id)
                .messages(emotionalRs.getMessages())
                .idEe(emotionalRs.getId())
                .duration(duration)
                .idUser(userId)
                .openAiId(chat.id())
                .fingerPrintOpenai(chat.systemFingerprint())
                .emotion(emotion)
                .keyphrases(getKeyphrases(chat))
                .tokens(getTokens(chat))
                .emotionEstimation(emotionalRs.getEmotions().toString())
                .build();
    }

    /**
     * get the tokens used generating the keyphrases.
     * @param chat to get the tokens.
     * @return {@link Map}, {@code key}: {@link String} type of token, {@code value}: {@link Integer} amount of tokens.
     */
    private static Map<String, Integer> getTokens(final ChatCompletion chat) {
        return Map.of("promptTokens", chat.usage().promptTokens(),
                    "totalTokens", chat.usage().totalTokens(),
                    "completionTokens", chat.usage().completionTokens());
    }

    /**
     * get the keyphrases from the chat result.
     * @param chat to get the keyphrases.
     * @return {@link List} of {@link String}.
     */
    private static List<String> getKeyphrases(final ChatCompletion chat) {
        List<String> result = new java.util.ArrayList<>();

        try {
            final Map keyphrases = new Gson()
                    .fromJson(chat.choices().getFirst().message().content(), Map.class);
            Stream.of(keyphrases)
                    .filter(Objects::nonNull)
                    .filter(k -> !k.isEmpty())
                    .filter(k -> k.containsKey("result"))
                    .findFirst()
                    .ifPresent(ke -> {
                        final List<String> keyphrasesResults = (List<String>) ke.get("result");
                        keyphrasesResults.parallelStream().forEach(result::add);
                    });
        } catch (Exception e) {
            log.error("[getKeyphrases]: Error getting the keyphrases: {}", e.getMessage());
        }

        return result;
    }

    /**
     * get a {@link KeyphraseOpenaiRqDto} from a {@link EmotionalClientRsDto} and an {@link EmotionEnum}.
     * @param emotionalRs emotions related to the messages.
     * @param emotion to get the keyphrases.
     * @return {@link KeyphraseOpenaiRqDto}.
     */
    public KeyphraseOpenaiRqDto getKeyphraseFromEmotionalRs(final EmotionalClientRsDto emotionalRs, final EmotionEnum emotion) {
        return KeyphraseOpenaiRqDto.builder()
                .emotion(emotion)
                .emotions(emotionalRs.getEmotions())
                .messages(emotionalRs.getMessages())
                .build();
    }

    /**
     * get {@link KeyphrasesRsDto} from an {@link KeyphrasesDao}.
     * @param dao to get the dto.
     * @param keyphrases  tto map the keyphrases.
     * @return {@link Optional} of {@link KeyphrasesRsDto}.
     */
    public static Optional<KeyphrasesRsDto> getRsFromDao(final KeyphrasesDao dao, final List<KeyphraseDao> keyphrases) {
        final Map emotions = new Gson().fromJson(dao.getEmotionEstimation(), Map.class);
        return getRsLisFromDaoList(keyphrases).map(keyphrasesRs -> KeyphrasesRsDto.builder()
                .keyphrases(keyphrasesRs)
                .emotion(EmotionEnum.valueOf(dao.getEmotion()))
                .id(dao.getId())
                .messages(dao.getMessages())
                .emotions(EmotionalDto.builder().emotions(emotions).build())
                .build());
    }

    /**
     * get {@link KeyphrasesRsDto} from an {@link KeyphrasesDao}.
     * @param dao to get the dto.
     * @param emotionId emotional identifier.
     * @return {@link Optional} of {@link KeyphrasesRsDto}.
     */
    public static Optional<KeyphrasesRsDto> getRsFromDao(final KeyphrasesDao dao, final UUID emotionId, final List<KeyphraseRsDto> keyphrases) {
        final Map emotions = new Gson().fromJson(dao.getEmotionEstimation(), Map.class);
        return Optional.of(KeyphrasesRsDto.builder()
                .keyphrases(keyphrases)
                .emotion(EmotionEnum.valueOf(dao.getEmotion()))
                .id(dao.getId())
                .messages(dao.getMessages())
                .emotions(EmotionalDto.builder().emotions(emotions).id(emotionId).build())
                .build());
    }

    /**
     * get a {@link List} of {@link KeyphraseRsDto} from a {@link List} of {@link KeyphraseDao}.
     * @param daos a {@link List} of {@link KeyphraseDao}.
     * @return {@link Optional} of {@link List} of {@link KeyphraseRsDto}.
     */
    public static Optional<List<KeyphraseRsDto>> getRsLisFromDaoList(final List<KeyphraseDao> daos) {
        List<KeyphraseRsDto> result = daos.parallelStream().map(KeyphraseMapper::getRsFromDao).toList();
        return Stream.of(result)
                .filter(r -> !r.isEmpty())
                .findFirst();
    }

    /**
     * get a {@link KeyphraseRsDto} from a {@link KeyphraseDao}.
     * @param dao a {@link KeyphraseDao}.
     * @return {@link KeyphraseRsDto}.
     */
    public static KeyphraseRsDto getRsFromDao(final KeyphraseDao dao) {
        return KeyphraseRsDto.builder()
                .id(dao.getId())
                .idKeyphrases(dao.getIdKeyphrases())
                .keyphrase(dao.getKeyphrase())
                .build();
    }
}
