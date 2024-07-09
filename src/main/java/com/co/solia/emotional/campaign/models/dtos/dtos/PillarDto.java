package com.co.solia.emotional.campaign.models.dtos.dtos;

import lombok.Builder;

import java.util.List;

/**
 * a pillar of campaign communication.
 * @param name the pillar name.
 * @param applications associated to the pillar.
 * @author luis.bolivar
 */
@Builder
public record PillarDto(
        String name,
        List<String> applications
) {
}
