package com.co.solia.emotional.customer.models.dtos.rs;

import lombok.Builder;

import java.util.UUID;

/**
 * response after create the customer.
 * @param id customer identifier.
 * @param email email of the customer.
 * @param name name of the customer.
 * @author luis.bolivar
 */
@Builder
public record CreateCustomerRsDto(
        UUID id,
        String email,
        String name
) {
}
