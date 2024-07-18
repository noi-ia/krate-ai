package com.co.solia.emotional.customer.models.mappers;

import com.co.solia.emotional.customer.models.daos.CustomerDao;
import com.co.solia.emotional.customer.models.dtos.rs.CreateCustomerRsDto;
import com.co.solia.emotional.share.models.validators.Validator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility class to map different class for customer.
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class CustomerMapper {

    /**
     * get a {@link CreateCustomerRsDto} from {@link CustomerDao}.
     * @param dao a {@link CustomerDao}
     * @return {@link Optional} of {@link CreateCustomerRsDto}
     */
    public static Optional<CreateCustomerRsDto> getFromDao(final CustomerDao dao) {
        return Stream.of(dao)
                .filter(Objects::nonNull)
                .filter(d -> Validator.isValidId(d.getId()))
                .filter(CustomerDao::getActive)
                .findFirst()
                .map(CustomerMapper::getRsFromDao);
    }

    /**
     * get a {@link CreateCustomerRsDto} from {@link CustomerDao}.
     * @param dao a {@link CustomerDao}
     * @return {@link CreateCustomerRsDto}
     */
    private static CreateCustomerRsDto getRsFromDao(final CustomerDao dao) {
        return CreateCustomerRsDto.builder()
                .id(dao.getId())
                .email(dao.getEmail())
                .name(dao.getName())
                .build();
    }
}
