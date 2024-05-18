package com.co.solia.emotional.emotional.models.repos;

import com.co.solia.emotional.emotional.models.daos.EmotionalDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * repository for {@link EmotionalDao}.
 */
@Repository
public interface EmotionalRepo extends MongoRepository<EmotionalDao, UUID> {

    /**
     * get a {@link Optional} of {@link List} of {@link EmotionalDao}.
     * @param idBatch batch emotional estimation identifier.
     * @return {@link Optional} of {@link List} of {@link EmotionalDao}.
     */
    @Query("{'idBatch': ?0}")
    List<EmotionalDao> findAllByIdBatch(UUID idBatch);
}
