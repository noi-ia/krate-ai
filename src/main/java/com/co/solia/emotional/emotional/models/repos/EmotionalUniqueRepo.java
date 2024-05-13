package com.co.solia.emotional.emotional.models.repos;

import com.co.solia.emotional.emotional.models.daos.EmotionalUniqueDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * repository to storage the emotional unique processing.
 *
 * @author luis.bolivar.
 */
@Repository
public interface EmotionalUniqueRepo extends MongoRepository<EmotionalUniqueDao, UUID> {
}
