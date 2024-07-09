package com.co.solia.emotional.campaign.models.dtos.dtos;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

/**
 * the brand dto to send to openai call.
 * @param id brand identifier.
 * @param name brand name.
 * @param description description of brand.
 * @param competitors list of competitors url.
 */
@Builder
public record BrandDto(
        UUID id,
        String name,
        String description,
        List<String> competitors
) {
}
