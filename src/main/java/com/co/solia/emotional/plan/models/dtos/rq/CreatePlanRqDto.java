package com.co.solia.emotional.plan.models.dtos.rq;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * request to create a new plan.
 * @param name name of the plan.
 * @param camsByMonth campaigns allowed by month.
 * @param refreshByCam amount of refresh by campaign.
 * @param priceMonth price of the month.
 * @param priceYear price of the year.
 * @author luis.bolivar.
 */
@Builder
public record CreatePlanRqDto(
        String name,
        Integer camsByMonth,
        Integer refreshByCam,
        BigDecimal priceMonth,
        BigDecimal priceYear
) {
}
