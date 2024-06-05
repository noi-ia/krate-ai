package com.co.solia.emotional.brand.services.impls;

import com.co.solia.emotional.brand.models.daos.BrandDao;
import com.co.solia.emotional.brand.models.dtos.rq.BrandRqDto;
import com.co.solia.emotional.brand.models.dtos.rs.BrandRsDto;
import com.co.solia.emotional.brand.models.mappers.BrandMapper;
import com.co.solia.emotional.brand.models.repos.BrandRepo;
import com.co.solia.emotional.brand.services.services.BrandService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * implementation service of {@link BrandService}.
 *
 * @author luis.bolivar.
 */
@Service
@Slf4j
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {

    /**
     * dependency on {@link BrandRepo}.
     */
    private BrandRepo brandRepo;

    /**
     * create a new brand,
     *
     * @param rq request to create the brand.
     * @return {@link Optional} of {@link BrandRsDto}.
     */
    @Override
    public Optional<BrandRsDto> create(final BrandRqDto rq) {
        log.info("[create]: ready to create the brand: {}", rq.getName());
        final UUID id = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        return BrandMapper.getDaoFromDto(rq, id, userId).flatMap(this::mapAndSave);
    }

    /**
     * map the dao to rs and save the brand
     * @param dao to save.
     * @return {@link Optional} of {@link BrandRsDto}.
     */
    public Optional<BrandRsDto> mapAndSave(final BrandDao dao) {
        return save(dao).flatMap(BrandMapper::getRsFromDao);
    }

    /**
     * save a brand.
     *
     * @param dao to save.
     * @return {@link Optional} of {@link BrandDao}.
     */
    @Override
    public Optional<BrandDao> save(final BrandDao dao) {
        Optional<BrandDao> result = Optional.empty();

        try {
            brandRepo.save(dao);
            log.info("[save]: brand save ok.");
            result = Optional.of(dao);
        } catch (Exception e) {
            log.error("[save]: error saving brand: {}", e.getMessage());
        }
        return result;
    }

    /**
     * get a brand by id.
     *
     * @param id to get the brand.
     * @return {@link Optional} of {@link BrandRsDto}.
     */
    @Override
    public Optional<BrandRsDto> getById(final UUID id) {
        log.info("[getById]: ready to get brand by id: {}", id);
        return findById(id).flatMap(BrandMapper::getRsFromDao);
    }

    /**
     * get a brand by id.
     *
     * @param id to get the brand.
     * @return {@link Optional} of {@link BrandRsDto}.
     */
    private Optional<BrandDao> findById(final UUID id) {
        Optional<BrandDao> result = Optional.empty();

        try {
            result = brandRepo.findById(id);
            log.info("[findById]: getting brand ok by id: {}", id);
        } catch (Exception e) {
            log.error("[findById]: error getting brand by id: {}, error: {}", id, e.getMessage());
        }
        return result;
    }

}
