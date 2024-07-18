package com.co.solia.emotional.customer.controllers.endpoints.v1;

import com.co.solia.emotional.customer.controllers.docs.CustomerControllerDocs;
import com.co.solia.emotional.customer.models.dtos.rq.CreateCustomerRqDto;
import com.co.solia.emotional.customer.models.dtos.rs.CreateCustomerRsDto;
import com.co.solia.emotional.customer.services.services.CustomerService;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.models.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * implementation of {@link CustomerControllerDocs} and endpoints for customer resource.
 * @author luis.bolivar.
 */
@RestController
@RequestMapping("/1/customer")
@AllArgsConstructor
@Slf4j
public class CustomerController implements CustomerControllerDocs {

    /**
     * dependency on {@link CustomerService}.
     */
    private CustomerService customerService;

    /**
     * create a new customer.
     *
     * @param rq payload to create a new customer.
     * @return {@link ResponseEntity} of {@link CreateCustomerRsDto}.
     */
    @Override
    @PostMapping("/")
    public ResponseEntity<CreateCustomerRsDto> create(@RequestBody final CreateCustomerRqDto rq) {
        return customerService.create(rq)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[create]: Error creating the customer with email: {}", rq.email());
                    throw InternalServerException.builder()
                            .endpoint("/customer/")
                            .message("Error creating the customer with email: {}%s".formatted(rq.email()))
                            .build();
                });
    }

    /**
     * get a customer by email.
     *
     * @param email to get the customer.
     * @return {@link ResponseEntity} of {@link CreateCustomerRsDto}.
     */
    @Override
    @GetMapping("/{email}")
    public ResponseEntity<CreateCustomerRsDto> getByEmail(@PathVariable("email") final String email) {
        return customerService.getByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[getByEmail]: Error getting the customer by email: {}", email);
                    throw NotFoundException.builder()
                            .endpoint("/customer/")
                            .message("Error getting the customer by email: {}%s".formatted(email))
                            .build();
                });
    }
}
