package com.co.solia.emotional.emotional.models.repos;

import com.co.solia.emotional.emotional.models.daos.EmotionalBatchDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * repository to persistence the {@link EmotionalBatchDao}.
 *
 * @author luis.bolivar
 */
@Repository
public interface EmotionalBatchRepo extends MongoRepository<EmotionalBatchDao, UUID> {
}
