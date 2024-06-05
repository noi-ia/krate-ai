package com.co.solia.emotional.brand.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * request to create a new brand specification.
 *
 * @author luis.bolivar.
 */
@ToString
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BrandRqDto {

    /**
     * name of brand.
     */
    private String name;

    /**
     * description of brand.
     */
    private String description;

    /**
     * links of competitors to parse to list, is optional.
     */
    private String competitors;
}
