package com.co.solia.emotional.customer.models.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * entity to map customer in the db.
 * @author luis.bolivar.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document("customer")
public class CustomerDao {

    /**
     * customer identifier.
     */
    @Id
    private UUID id;

    /**
     * email of the customer.
     */
    private String email;

    /**
     * name of the customer.
     */
    private String name;

    /**
     * created date of the customer.
     */
    private Long created;

    /**
     * the customer is active.
     */
    private Boolean active;

    /**
     * the last updated on the customer.
     */
    private Long updated;
}
