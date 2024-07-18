package com.co.solia.emotional.customer.models.repos;

import com.co.solia.emotional.customer.models.daos.CustomerDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * repository tto handle the data about customer with the db.
 * @author luis.bolivar.
 */
@Repository
public interface CustomerRepo extends MongoRepository<CustomerDao, UUID> {

    /**
     * find the customer by email.
     * @param email to get the customer.
     * @return {@link Optional} of {@link CustomerDao}.
     */
    Optional<CustomerDao> findByEmail(String email);
}
