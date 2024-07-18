package com.co.solia.emotional.customer.services.impls;

import com.co.solia.emotional.customer.models.daos.CustomerDao;
import com.co.solia.emotional.customer.models.dtos.rq.CreateCustomerRqDto;
import com.co.solia.emotional.customer.models.dtos.rs.CreateCustomerRsDto;
import com.co.solia.emotional.customer.models.mappers.CustomerMapper;
import com.co.solia.emotional.customer.models.repos.CustomerRepo;
import com.co.solia.emotional.customer.services.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * implementation of {@link CustomerService}.
 *
 * @author luis.bolivar.
 */
@AllArgsConstructor
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    /**
     * dependency on the {@link CustomerRepo}.
     */
    private CustomerRepo customerRepo;

    /**
     * create a new customer.
     *
     * @param rq payload for creating the new customer.
     * @return {@link Optional} of {@link CreateCustomerRsDto}
     */
    @Override
    public Optional<CreateCustomerRsDto> create(final CreateCustomerRqDto rq) {
        return Optional.empty();
    }

    /**
     * get a customer by its email.
     *
     * @param email to get the customer by email.
     * @return {@link Optional} of {@link CreateCustomerRsDto}
     */
    @Override
    public Optional<CreateCustomerRsDto> getByEmail(final String email) {
        log.info("[getByEmail]: ready to get the customer by email: {}", email);
        return getCustomerById(email).flatMap(CustomerMapper::getFromDao);
    }

    /**
     * get a customer by its email.
     * @param email to get the customer by email.
     * @return {@link Optional} of {@link CreateCustomerRsDto}
     */
    private Optional<CustomerDao> getCustomerById(final String email) {
        Optional<CustomerDao> result = Optional.empty();

        try {
            result = customerRepo.findByEmail(email);
            log.info("[getCustomerById]: Customer found ok by email: {}", email);
        } catch (Exception e) {
            log.error("[getCustomerById]: Error finding customer by email: {}, with error: {}", email, e.getMessage());
        }

        return result;
    }
}
