package com.co.solia.emotional.campaign.models.mappers;

import com.co.solia.emotional.campaign.models.dtos.dtos.BrandDto;
import com.co.solia.emotional.campaign.models.dtos.rs.BrandClientRsDto;
import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.Optional;

/**
 * Brand mapper for campaign resource.
 *
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class BrandMapper {

    /**
     * get a brand from a {@link Response}
     * @param rs to get the brand.
     * @return {@link Optional} of {@link BrandClientRsDto}.
     */
    public static Optional<BrandClientRsDto> getFromRs(final Response rs) {
        Optional<BrandClientRsDto> result = Optional.empty();
        try {
            final BrandClientRsDto brand = new Gson().fromJson(rs.body().string(), BrandClientRsDto.class);
            result = brand != null && brand.id() != null ? Optional.of(brand) : Optional.empty();
            log.info("[getFromRs]: data getting ok.");
        } catch(Exception e) {
            log.error("[getFromRs] error parsing the response: {}", e.getMessage());
        }
        return result;
    }

    /**
     * from a {@link BrandClientRsDto} get a {@link BrandDto}.
     * @param rs the {@link BrandClientRsDto}.
     * @return {@link BrandDto}
     */
    public static BrandDto getDtoFromRs(final BrandClientRsDto rs){
        return BrandDto.builder()
                .id(rs.id())
                .name(rs.name())
                .competitors(rs.competitors())
                .description(rs.description())
                .build();
    }
}
