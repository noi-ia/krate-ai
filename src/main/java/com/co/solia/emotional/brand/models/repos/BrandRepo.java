package com.co.solia.emotional.brand.models.repos;

import com.co.solia.emotional.brand.models.daos.BrandDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * connection with db to {@link BrandDao}.
 *
 * @author luis.bolivar.
 */
@Repository
public interface BrandRepo extends MongoRepository <BrandDao, UUID> {
}
