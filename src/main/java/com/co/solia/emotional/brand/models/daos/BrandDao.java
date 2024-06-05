package com.co.solia.emotional.brand.models.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Entity to save in db the brand.
 *
 * @author luis.bolivar
 */
@Builder
@Getter
@Document("brand")
@AllArgsConstructor
@NoArgsConstructor
public class BrandDao {
    /**
     * id of brand.
     */
    @Id
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
     * links of competitors to parse to list.
     */
    private List<String> competitors;

    /**
     * user identifier.
     */
    private UUID idUser;

    /**
     * is active this processing to still reviewing.
     */
    @Builder.Default
    private Boolean activate = Boolean.TRUE;

    /**
     * date of brand created.
     */
    @Builder.Default
    private long created = Instant.now().getEpochSecond();
}
