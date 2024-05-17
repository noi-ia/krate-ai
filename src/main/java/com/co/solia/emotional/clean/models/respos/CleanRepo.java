package com.co.solia.emotional.clean.models.respos;

import com.co.solia.emotional.clean.models.daos.CleanDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * interface to persistence the {@link CleanDao}.
 *
 * @author luis.bolivar
 */

@Repository
public interface CleanRepo extends MongoRepository<CleanDao, UUID> {
}
