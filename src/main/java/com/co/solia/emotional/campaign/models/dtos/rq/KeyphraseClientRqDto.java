package com.co.solia.emotional.campaign.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * request to call the keyphrase service.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KeyphraseClientRqDto {

    /**
     * list of messages.
     */
    private List<String> messages;
}
