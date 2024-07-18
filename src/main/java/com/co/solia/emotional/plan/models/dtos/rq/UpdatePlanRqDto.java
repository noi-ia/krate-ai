package com.co.solia.emotional.plan.models.dtos.rq;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * request to update the pricing in the plan.
 * @param priceMonth price by month.
 * @param priceYear price by year.
 * @author luis.bolivar.
 */
@Builder
public record UpdatePlanRqDto(
        BigDecimal priceMonth,
        BigDecimal priceYear
) {
}
