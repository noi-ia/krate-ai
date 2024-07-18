package com.co.solia.emotional.plan.models.repos;

import com.co.solia.emotional.plan.models.daos.PlanDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * connect the plan entity with the db.
 * @author luis.bolivar.
 */
@Repository
public interface PlanRepo extends MongoRepository<PlanDao, UUID> {

    /**
     * find a plan by name.
     * @param name to find the plan.
     * @return {@link Optional} of {@link PlanDao}.
     */
    Optional<PlanDao> findByName(String name);
}