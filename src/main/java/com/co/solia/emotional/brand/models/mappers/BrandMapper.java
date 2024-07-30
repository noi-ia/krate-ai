package com.co.solia.emotional.brand.models.mappers;

import com.co.solia.emotional.brand.models.daos.BrandDao;
import com.co.solia.emotional.brand.models.dtos.rq.BrandRqDto;
import com.co.solia.emotional.brand.models.dtos.rs.BrandRsDto;
import com.co.solia.emotional.share.utils.validators.Validator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * mapper for brand.
 *
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class BrandMapper {

    /**
     * get a dao from the rq
     * @param rq to get the dao.
     * @return {@link Optional} of {@link BrandDao}.
     */
    public static Optional<BrandDao> getDaoFromDto(
            final BrandRqDto rq,
            final UUID id,
            final UUID userId) {
        return Stream.of(rq)
                .filter(Objects::nonNull)
                .filter(r -> Validator.isValidString(r.getName()))
                .filter(r -> Validator.isValidString(r.getCompetitors()))
                .filter(r -> Validator.isValidString(r.getDescription()))
                .findFirst()
                .map(r -> getDaoFrom(r, id, userId));
    }

    /**
     * get a {@link BrandRsDto} from {@link BrandDao}.
     * @param brand to get the {@link BrandRsDto}.
     * @return {@link Optional} of {@link BrandRsDto}.
     */
    public static Optional<BrandRsDto> getRsFromDao(final BrandDao brand) {
        return Stream.of(brand)
                .filter(Objects::nonNull)
                .filter(dao -> dao.getId() != null)
                .findFirst()
                .map(BrandMapper::getRsFrom);
    }

    /**
     * get a dao from the rq
     * @param rq to get the dao.
     * @return {@link BrandDao}.
     */
    public static BrandDao getDaoFrom(final BrandRqDto rq, final UUID id, final UUID userId) {
        return BrandDao.builder()
                .id(id)
                .name(rq.getName())
                .description(rq.getDescription())
                .competitors(getCompetitors(rq))
                .idUser(userId)
                .build();
    }

    /**
     * get links of competitors
     * @param rq to get the competitors
     * @return {@link List} of {@link String}
     */
    private static List<String> getCompetitors(final BrandRqDto rq) {
        return Arrays.stream(rq.getCompetitors().trim().split(",")).toList();
    }

    /**
     * get a {@link BrandRsDto} from {@link BrandDao}.
     * @param brand to get the {@link BrandRsDto}.
     * @return {@link BrandRsDto}.
     */
    private static BrandRsDto getRsFrom(final BrandDao brand) {
        return BrandRsDto.builder()
                .competitors(brand.getCompetitors())
                .description(brand.getDescription())
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}
