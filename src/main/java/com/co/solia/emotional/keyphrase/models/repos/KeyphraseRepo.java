package com.co.solia.emotional.keyphrase.models.repos;

import com.co.solia.emotional.keyphrase.models.daos.KeyphraseDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * repository of keyphrase process.
 */
@Repository
public interface KeyphraseRepo extends MongoRepository<KeyphraseDao, UUID> {
}
