package com.co.solia.emotional.campaign.clients.impls;

import com.co.solia.emotional.campaign.clients.clients.BrandClient;
import com.co.solia.emotional.campaign.models.dtos.rs.BrandClientRsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * implementation of {@link BrandClient}.
 *
 * @author luis.bolivar.
 */
@Component
@Slf4j
public class BrandClientImpl implements BrandClient {
    /**
     * get a brand by id.
     *
     * @param id to get the brand.
     * @return {@link Optional} of {@link BrandClientRsDto}
     */
    @Override
    public Optional<BrandClientRsDto> getById(final UUID id) {
        return Optional.empty();
    }
}
