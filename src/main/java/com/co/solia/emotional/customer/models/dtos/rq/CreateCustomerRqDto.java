package com.co.solia.emotional.customer.models.dtos.rq;


import lombok.Builder;

/**
 * the request for create a new customer.
 * @param email identity of the customer.
 * @param name name of the customer.
 * @author luis.bolivar.
 */
@Builder
public record CreateCustomerRqDto(
        String email,
        String name
) {
}
