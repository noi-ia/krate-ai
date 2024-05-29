package com.co.solia.emotional.keyphrase.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * request to generate the keyphrase.
 *
 * @author luis.bolivar.
 */

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeyphraseRqDto {

    /**
     * attribute.
     */
    private List<String> messages;
}
