package com.co.solia.emotional.keyphrase.models.repos;

import com.co.solia.emotional.keyphrase.models.daos.KeyphraseDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * repository for just one keyphrase, for associate to multiple keyphrases.
 *
 * @author luis.bolivar.
 */
@Repository
public interface KeyphraseRepo extends MongoRepository<KeyphraseDao, UUID> {

    /**
     * get a {@link Optional} of {@link List} of {@link KeyphraseDao}.
     * @param idKeyphrases to get the keyphrases.
     * @return {@link List} of {@link KeyphraseDao}.
     */
    @Query("{'idBatch': ?0}")
    List<KeyphraseDao> findAllByIdKeyphrases(UUID idKeyphrases);
}
