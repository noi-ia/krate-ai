package com.co.solia.emotional.plan.models.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entity to save in db the plan.
 *
 * @author luis.bolivar
 */
@Builder
@Getter
@Document("plan")
@AllArgsConstructor
@NoArgsConstructor
public class PlanDao {

    /**
     * plan identifier.
     */
    @Id
    private UUID id;

    /**
     * the plan name.
     */
    private String name;

    /**
     * amount of campaigns by month.
     */
    private Integer camsByMonth;

    /**
     * amount refresh to generate a new campaign.
     */
    private Integer refreshByCam;

    /**
     * price by month.
     */
    private BigDecimal priceMonth;

    /**
     * price by year.
     */
    private BigDecimal priceYear;

    /**
     * date of creation.
     */
    @Builder.Default
    private Long created = System.currentTimeMillis();

    /**
     * the plan start active.
     */
    @Builder.Default
    private Boolean active = Boolean.TRUE;

    /**
     * the last update date.
     */
    private Long updated;
}
