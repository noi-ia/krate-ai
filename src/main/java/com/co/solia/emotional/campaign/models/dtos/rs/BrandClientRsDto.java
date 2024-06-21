package com.co.solia.emotional.campaign.models.dtos.rs;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

/**
 * brand result from get by id.
 * @param id brand identifier.
 * @param name brand name.
 * @param description brand description.
 * @param competitors brand competitors.
 */
@Builder
public record BrandClientRsDto(
        UUID id,
        String name,
        String description,
        List<String>competitors
) {
}
