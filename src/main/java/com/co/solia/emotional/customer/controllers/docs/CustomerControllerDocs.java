package com.co.solia.emotional.customer.controllers.docs;

import com.co.solia.emotional.customer.models.dtos.rq.CreateCustomerRqDto;
import com.co.solia.emotional.customer.models.dtos.rs.CreateCustomerRsDto;
import org.springframework.http.ResponseEntity;

/**
 * It's the api documentation for swagger.
 *
 * @author luis.bolivar.
 */
public interface CustomerControllerDocs {

    /**
     * create a new customer.
     * @param rq payload to create a new customer.
     * @return {@link ResponseEntity} of {@link CreateCustomerRsDto}.
     */
    ResponseEntity<CreateCustomerRsDto> create(CreateCustomerRqDto rq);

    /**
     * get a customer by email.
     * @param email to get the customer.
     * @return {@link ResponseEntity} of {@link CreateCustomerRsDto}.
     */
    ResponseEntity<CreateCustomerRsDto> getByEmail(String email);
}
