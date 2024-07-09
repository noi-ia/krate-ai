package com.co.solia.emotional.keyphrase.services.impls;

import com.co.solia.emotional.keyphrase.models.daos.KeyphraseDao;
import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphraseRsDto;
import com.co.solia.emotional.keyphrase.models.repos.KeyphraseRepo;
import com.co.solia.emotional.share.clients.clients.EmotionalClient;
import com.co.solia.emotional.keyphrase.models.daos.KeyphrasesDao;
import com.co.solia.emotional.keyphrase.models.dtos.rq.KeyphraseRqDto;
import com.co.solia.emotional.share.models.dtos.rs.EmotionalClientRsDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphrasesRsDto;
import com.co.solia.emotional.keyphrase.models.enums.EmotionEnum;
import com.co.solia.emotional.keyphrase.models.mappers.KeyphraseMapper;
import com.co.solia.emotional.keyphrase.models.repos.KeyphrasesRepo;
import com.co.solia.emotional.keyphrase.services.services.KeyphraseService;
import com.co.solia.emotional.share.models.validators.BasicValidator;
import com.co.solia.emotional.share.services.services.OpenAIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
     * dependency on {@link KeyphrasesRepo}.
     */
    private KeyphrasesRepo keyphrasesRepo;

    /**
     * dependency on {@link KeyphraseRepo}.
     */
    private KeyphraseRepo keyphraseRepo;

    /**
     * {@inheritDoc}.
     * @param keyphraseRq request to compute.
     * @param emotion emotion to generate the keyphrase.
     * @return {@link Optional} of {@link KeyphrasesRsDto}.
     */
    @Override
    public Optional<KeyphrasesRsDto> compute(final KeyphraseRqDto keyphraseRq, final EmotionEnum emotion) {
        final UUID userId = UUID.randomUUID();
        return computeEmotionalEstimation(keyphraseRq)
                .flatMap(emotionalRs -> computeKeyphrases(emotionalRs, emotion, userId));
    }

    /**
     * {@inheritDoc}.
     * @param id to get the keyphrase.
     * @return
     */
    @Override
    public Optional<KeyphrasesRsDto> getById(final UUID id) {
        log.info("[getById]: get the keyphrase by id: {}", id);
        return getKeyphrasesById(id).flatMap(keyphrases ->
            getKeyPhrasesById(id).flatMap(keyphraseList -> KeyphraseMapper.getRsFromDao(keyphrases, keyphraseList)));
    }

    /**
     * get the emotional results.
     * @param keyphraseRq rq to get the emotional results.
     * @return {@link Optional} of {@link EmotionalClientRsDto}.
     */
    private Optional<EmotionalClientRsDto> computeEmotionalEstimation(final KeyphraseRqDto keyphraseRq) {
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
     * @return {@link Optional} of {@link KeyphrasesRsDto}.
     */
    private Optional<KeyphrasesRsDto> computeKeyphrases(
            final EmotionalClientRsDto emotionalRs,
            final EmotionEnum emotion,
            final UUID userId) {
        final UUID id = UUID.randomUUID();
        final long start = BasicValidator.getNow();
        log.info("[computeKeyphrases]: ready to get the keyphrases.");
        return computeKeyphrases(emotionalRs, emotion)
                .flatMap(chat -> mapAndSaveKeyphrases(emotionalRs, emotion, userId, chat, id, start));
    }

    /**
     * map and save all keyphrase process.
     * @param emotionalRs emotional results.
     * @param emotion emotion to associate the keyphrases.
     * @param userId user identifier.
     * @param chat result from openai.
     * @param id process identifier.
     * @param start date when started.
     * @return {@link Optional} of {@link KeyphrasesRsDto}.
     */
    private Optional<KeyphrasesRsDto> mapAndSaveKeyphrases(
            final EmotionalClientRsDto emotionalRs,
            final EmotionEnum emotion,
            final UUID userId,
            final ChatCompletion chat,
            final UUID id,
            final long start) {
        return mapAndSave(emotionalRs, emotion, userId, chat, id, getDuration(start))
                .flatMap(dao -> mapAndSaveEachKeyphrase(dao, id)
                                .flatMap(keyphrases -> KeyphraseMapper.getRsFromDao(dao, emotionalRs.getId(), keyphrases)));
    }

    /**
     * map and save each keyphrase.
     * @param dao to get the keyphrases.
     * @param id to associate to the keyphrase process.
     * @return {@link Optional} of {@link List} of {@link KeyphraseRsDto}.
     */
    private Optional<List<KeyphraseRsDto>> mapAndSaveEachKeyphrase(final KeyphrasesDao dao, final UUID id) {
        return getKeyphrases(dao, id).flatMap(daos -> {
            daos.parallelStream().forEach(this::saveKeyphrase);
            return KeyphraseMapper.getRsLisFromDaoList(daos);
        });
    }

    /**
     * get the list of {@link KeyphraseDao} for processing each keyphrase.
     * @param dao to get the keyphrases.
     * @param id o associate to keyphrase process.
     * @return {@link Optional} of {@link List} of {@link KeyphraseDao}.
     */
    private Optional<List<KeyphraseDao>> getKeyphrases(final KeyphrasesDao dao, final UUID id) {
        final List<KeyphraseDao> result = dao.getKeyphrases().parallelStream()
                .map(keyphrase -> generateKeyphrase(keyphrase, id)).toList();
        return Stream.of(result)
                .filter(r -> !r.isEmpty())
                .findFirst();
    }

    /**
     * get a {@link KeyphraseDao} from the {@link String} plain keyphrase and id.
     * @param keyphrase plain text.
     * @param id to associated to keyphrases.
     * @return {@link KeyphraseDao}.
     */
    private KeyphraseDao generateKeyphrase(final String keyphrase, final UUID id) {
        return KeyphraseDao.builder()
                .id(UUID.randomUUID())
                .idKeyphrases(id)
                .keyphrase(keyphrase)
                .build();
    }

    /**
     * call to {@link OpenAIService} to generate the keyphrases.
     * @param emotionalRs the emotional estimation to generate the keyphrases.
     * @param emotion emotion associated for generate the keyphrases.
     * @return {@link Optional} of {@link ChatCompletion}.
     */
    private Optional<ChatCompletion> computeKeyphrases(final EmotionalClientRsDto emotionalRs, final EmotionEnum emotion) {
        return openAIService.getKeyphrases(KeyphraseMapper.getKeyphraseFromEmotionalRs(emotionalRs, emotion));
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
     * @return {@link Optional} of {@link KeyphrasesDao}.
     */
    private Optional<KeyphrasesDao> mapAndSave(
            final EmotionalClientRsDto emotionalRs,
            final EmotionEnum emotion,
            final UUID userId,
            final ChatCompletion chat,
            final UUID id,
            final long duration) {
        return KeyphraseMapper.getDaoFromChatCompletion(chat, id, emotionalRs, duration, userId, emotion.toString())
                .flatMap(this::saveKeyphrase);
    }

    /**
     * save the keyphrases.
     * @param dao to save.
     */
    @Override
    public Optional<KeyphrasesDao> saveKeyphrase(final KeyphrasesDao dao) {
        Optional<KeyphrasesDao> result = Optional.empty();
        try {
            keyphrasesRepo.save(dao);
            log.info("[save]: save keyphrases ok.");
            result = Optional.of(dao);
        } catch (Exception e) {
            log.error("[save] error saving keyphrases: {}", e.getMessage());
        }

        return result;
    }

    /**
     * save the dao.
     *
     * @param dao to save.
     */
    @Override
    public Optional<KeyphraseDao> saveKeyphrase(final KeyphraseDao dao) {
        Optional<KeyphraseDao> result = Optional.empty();
        try {
            keyphraseRepo.save(dao);
            log.info("[saveKeyphrase]: save dao ok.");
            result = Optional.of(dao);
        } catch (Exception e) {
            log.error("[saveKeyphrase] error saving dao: {}", e.getMessage());
        }
        return result;
    }

    /**
     * get a {@link List} of {@link KeyphraseDao} by keyphrases id.
     *
     * @param id to get the {@link List} of {@link KeyphraseDao}.
     * @return {@link Optional} of {@link List} of {@link KeyphraseDao}.
     */
    @Override
    public Optional<List<KeyphraseDao>> getKeyPhrasesById(final UUID id) {
        Optional<List<KeyphraseDao>> result = Optional.empty();
        try {
            final List<KeyphraseDao> list = keyphraseRepo.findAllByIdKeyphrases(id);
            result = list != null && !list.isEmpty() ? Optional.of(list) : Optional.empty();
        } catch (Exception e) {
            log.error("[getKeyPhrasesById]: error getting the keyphrases by id: {}, with error: {}", id, e.getMessage());
        }
        return result;
    }

    /**
     * get a keyphrase process by id.
     *
     * @param id to get the keyphrase.
     * @return {@link Optional} of {@link KeyphraseRsDto}.
     */
    @Override
    public Optional<KeyphraseRsDto> getKeyphraseById(final UUID id) {
        log.info("[getKeyphraseById]: ready to get the keyphrase by id: {}", id);
        return getKeyphraseByKeyphraseId(id).map(KeyphraseMapper::getRsFromDao);
    }

    /**
     * get a keyphrase process by id.
     *
     * @param id to get the keyphrase.
     * @return {@link Optional} of {@link KeyphraseRsDto}.
     */
    private Optional<KeyphraseDao> getKeyphraseByKeyphraseId(final UUID id) {
        Optional<KeyphraseDao> result = Optional.empty();
        try {
            result = keyphraseRepo.findById(id);
            log.info("[getKeyphraseByKeyphraseId]: found keyphrase ok.");
        } catch (Exception e) {
            log.error("[getKeyphraseByKeyphraseId]: error getting keyphrase by id: {}, with error: {}", id, e.getMessage());
        }
        return result;
    }

    /**
     * get the keyphrase by id from db.
     * @param id to get the keyphrase from db.
     * @return {@link Optional} of {@link KeyphrasesDao}.
     */
    private Optional<KeyphrasesDao> getKeyphrasesById(final UUID id) {
        Optional<KeyphrasesDao> result = Optional.empty();

        try {
            result = keyphrasesRepo.findById(id);
            log.info("[getKeyphraseById]");
        } catch (Exception e) {
            log.error("[getKeyphraseById]: error getting keyphrase by id: {}, error: {}", id, e.getMessage());
        }

        return result;
    }
}
