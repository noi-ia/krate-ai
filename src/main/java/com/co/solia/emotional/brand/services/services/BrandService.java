package com.co.solia.emotional.brand.services.services;

import com.co.solia.emotional.brand.models.daos.BrandDao;
import com.co.solia.emotional.brand.models.dtos.rq.BrandRqDto;
import com.co.solia.emotional.brand.models.dtos.rs.BrandRsDto;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface to provide brand services behavior.
 *
 * @author luis.bolivar.
 */
public interface BrandService {

    /**
     * create a new brand,
     * @param rq request to create the brand.
     * @return {@link Optional} of {@link BrandRsDto}.
     */
    Optional<BrandRsDto> create(BrandRqDto rq);

    /**
     *
     * save a brand.
     * @param dao to save.
     * @return {@link Optional} of {@link BrandDao}.
     */
    Optional<BrandDao> save(BrandDao dao);

    /**
     * get a brand by id.
     * @param id to get the brand.
     * @return {@link Optional} of {@link BrandRsDto}.
     */
    Optional<BrandRsDto> getById(UUID id);
}
