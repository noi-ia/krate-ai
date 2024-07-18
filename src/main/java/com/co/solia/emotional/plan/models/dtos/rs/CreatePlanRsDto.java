package com.co.solia.emotional.plan.models.dtos.rs;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * response of the plan creation.
 * @param id plan identifier.
 * @param name name of the plan.
 * @param camsByMonth campaigns allowed by month.
 * @param refreshByCam amount of refresh by campaign.
 * @param priceMonth price of the month.
 * @param priceYear price of the year.
 * @author luis.bolivar.
 */
@Builder
public record CreatePlanRsDto(
        UUID id,
        String name,
        Integer camsByMonth,
        Integer refreshByCam,
        BigDecimal priceMonth,
        BigDecimal priceYear
) {
}
