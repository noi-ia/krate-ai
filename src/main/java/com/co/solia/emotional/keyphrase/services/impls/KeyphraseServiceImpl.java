package com.co.solia.emotional.keyphrase.services.impls;

import com.co.solia.emotional.share.clients.clients.EmotionalClient;
import com.co.solia.emotional.keyphrase.models.daos.KeyphraseDao;
import com.co.solia.emotional.keyphrase.models.dtos.rq.KeyphraseRqDto;
import com.co.solia.emotional.share.models.dtos.rs.EmotionalClientRsDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphraseRsDto;
import com.co.solia.emotional.keyphrase.models.enums.EmotionEnum;
import com.co.solia.emotional.keyphrase.models.mappers.KeyphraseMapper;
import com.co.solia.emotional.keyphrase.models.repos.KeyphraseRepo;
import com.co.solia.emotional.keyphrase.services.services.KeyphraseService;
import com.co.solia.emotional.share.models.validators.BasicValidator;
import com.co.solia.emotional.share.services.services.OpenAIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * implementation of {@link KeyphraseService}.
 *
 * @author luis.bolivar.
 */
@Service
@Slf4j
@AllArgsConstructor
public class KeyphraseServiceImpl implements KeyphraseService {

    /**
     * dependency on {@link EmotionalClient}.
     */
    private EmotionalClient emotionalClient;

    /**
     * dependency on {@link OpenAIService}.
     */
    private OpenAIService openAIService;

    /**
     * dependency on {@link KeyphraseRepo}.
     */
    private KeyphraseRepo keyphraseRepo;

    /**
     * {@inheritDoc}.
     * @param keyphraseRq request to compute.
     * @param emotion emotion to generate the keyphrase.
     * @return {@link Optional} of {@link KeyphraseRsDto}.
     */
    @Override
    public Optional<KeyphraseRsDto> compute(final KeyphraseRqDto keyphraseRq, final EmotionEnum emotion) {
        final UUID userId = UUID.randomUUID();
        return getEmotionalResults(keyphraseRq)
                .flatMap(emotionalRs -> computeKeyphrases(emotionalRs, emotion, userId));
    }

    /**
     * {@inheritDoc}.
     * @param id to get the keyphrase.
     * @return
     */
    @Override
    public Optional<KeyphraseRsDto> getById(final UUID id) {
        log.info("[getById]: get the keyphrase by id: {}", id);
        return getKeyphraseById(id).flatMap(KeyphraseMapper::getRsFromDao);
    }

    /**
     * get the emotional results.
     * @param keyphraseRq rq to get the emotional results.
     * @return {@link Optional} of {@link EmotionalClientRsDto}.
     */
    private Optional<EmotionalClientRsDto> getEmotionalResults(final KeyphraseRqDto keyphraseRq) {
        log.info("[getEmotionalResults]: ready to get the emotional estimation of {} messages.",
                keyphraseRq.getMessages().size());
        return KeyphraseMapper.getFromRqDto(keyphraseRq)
                .flatMap(emotionalClient::compute);
    }

    /**
     * compute the keyphrases.
     * @param emotionalRs emotional results.
     * @param emotion emotion to compute the keyphrases.
     * @param userId user identifier.
     * @return {@link Optional} of {@link KeyphraseRsDto}.
     */
    private Optional<KeyphraseRsDto> computeKeyphrases(final EmotionalClientRsDto emotionalRs, final EmotionEnum emotion, final UUID userId) {
        final UUID id = UUID.randomUUID();
        final long start = BasicValidator.getNow();
        log.info("[computeKeyphrases]: ready to get the keyphrases.");
        return openAIService.getKeyphrases(KeyphraseMapper.getKeyphraseFromEmotionalRs(emotionalRs, emotion))
                .flatMap(chat -> mapAndSave(emotionalRs, emotion, userId, chat, id, getDuration(start))
                            .flatMap(dao -> KeyphraseMapper.getRsFromDao(dao, emotionalRs.getId())));
    }

    /**
     * get duration of keyphrase process.
     * @param start time.
     * @return duration of keyphrase process.
     */
    private static long getDuration(long start) {
        return BasicValidator.getDuration(start);
    }

    /**
     * map and save the keyphrases.
     * @param emotionalRs emotional result.
     * @param emotion emotion to get the keyphrases.
     * @param userId user identifier.
     * @param chat result from openai.
     * @param id process identifier.
     * @param duration duration of process.
     * @return {@link Optional} of {@link KeyphraseDao}.
     */
    private Optional<KeyphraseDao> mapAndSave(
            final EmotionalClientRsDto emotionalRs,
            final EmotionEnum emotion,
            final UUID userId,
            final ChatCompletion chat,
            final UUID id,
            final long duration) {
        return KeyphraseMapper.getDaoFromChatCompletion(chat, id, emotionalRs, duration, userId, emotion.toString())
                .map(dao -> {
                    save(dao);
                    return dao;
                });
    }

    /**
     * save the keyphrase.
     * @param dao to save.
     */
    private void save(final KeyphraseDao dao) {
        try {
            keyphraseRepo.save(dao);
            log.info("[save]: save keyphrases ok.");
        } catch (Exception e) {
            log.error("[save] error saving keyphrases: {}", e.getMessage());
        }
    }

    /**
     * get the keyphrase by id from db.
     * @param id to get the keyphrase from db.
     * @return {@link Optional} of {@link KeyphraseDao}.
     */
    private Optional<KeyphraseDao> getKeyphraseById(final UUID id) {
        Optional<KeyphraseDao> result = Optional.empty();

        try {
            result = keyphraseRepo.findById(id);
            log.info("[getKeyphraseById]");
        } catch (Exception e) {
            log.error("[getKeyphraseById]: error getting keyphrase by id: {}, error: {}", id, e.getMessage());
        }

        return result;
    }
}
