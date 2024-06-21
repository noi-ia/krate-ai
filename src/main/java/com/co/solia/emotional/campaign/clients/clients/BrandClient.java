package com.co.solia.emotional.campaign.clients.clients;

import com.co.solia.emotional.campaign.models.dtos.rs.BrandClientRsDto;

import java.util.Optional;
import java.util.UUID;

/**
 * the interface to map the calls to the brand service.
 *
 * @author luis.bolivar.
 */
public interface BrandClient {

    /**
     * get a brand by id.
     * @param id to get the brand.
     * @return {@link Optional} of {@link BrandClientRsDto}
     */
    Optional<BrandClientRsDto> getById(UUID id);
}
