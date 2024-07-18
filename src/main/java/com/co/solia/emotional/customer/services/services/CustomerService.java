package com.co.solia.emotional.customer.services.services;

import com.co.solia.emotional.customer.models.dtos.rq.CreateCustomerRqDto;
import com.co.solia.emotional.customer.models.dtos.rs.CreateCustomerRsDto;

import java.util.Optional;

/**
 * mapping of the customer services.
 *
 * @author luis.bolivar.
 */
public interface CustomerService {

    /**
     * create a new customer.
     * @param rq payload for creating the new customer.
     * @return {@link Optional} of {@link CreateCustomerRsDto}
     */
    Optional<CreateCustomerRsDto> create(CreateCustomerRqDto rq);

    /**
     * get a customer by its email.
     * @param email to get the customer by email.
     * @return {@link Optional} of {@link CreateCustomerRsDto}
     */
    Optional<CreateCustomerRsDto> getByEmail(String email);
}
