package com.co.solia.emotional.clean.models.repos;

import com.co.solia.emotional.clean.models.daos.CleanDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * interface to persistence the {@link CleanDao}.
 *
 * @author luis.bolivar
 */
@Repository
public interface CleanRepo extends MongoRepository<CleanDao, UUID> {

    /**
     * get a {@link Optional} of {@link List} of {@link CleanDao}.
     * @param idBatch batch clean identifier.
     * @return {@link Optional} of {@link List} of {@link CleanDao}.
     */
    @Query("{'idBatch': ?0}")
    List<CleanDao> findAllByIdBatch(UUID idBatch);
}
