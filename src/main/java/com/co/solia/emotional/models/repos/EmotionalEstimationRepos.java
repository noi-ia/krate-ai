package com.co.solia.emotional.models.repos;

import com.co.solia.emotional.models.daos.EmotionalEstimationDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * repository for {@link EmotionalEstimationDao}.
 */
@Repository
public interface EmotionalEstimationRepos extends MongoRepository<EmotionalEstimationDao, UUID> {
}
