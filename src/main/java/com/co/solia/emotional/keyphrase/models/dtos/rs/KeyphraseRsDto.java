package com.co.solia.emotional.keyphrase.models.dtos.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.UUID;

/**
 * response dto for a keyphrase.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class KeyphraseRsDto {

    /**
     * identifier.
     */
    private UUID id;

    /**
     * a keyphrase.
     */
    private String keyphrase;
}
