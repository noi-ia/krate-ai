package com.co.solia.emotional.brand.models.dtos.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

/**
 * response of brand creation.
 *
 * @author luis.bolivar.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class BrandRsDto {

    private UUID id;

    /**
     * name of brand.
     */
    private String name;

    /**
     * description of brand.
     */
    private String description;

    /**
     * competitors links lists.
     */
    private List<String> competitors;
}
