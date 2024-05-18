package com.co.solia.emotional.clean.models.repos;

import com.co.solia.emotional.clean.models.daos.CleanBatchDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * interface to persistence the {@link CleanBatchDao}.
 *
 * @author luis.bolivar
 */
@Repository
public interface CleanBatchRepo extends MongoRepository<CleanBatchDao, UUID> {
}
