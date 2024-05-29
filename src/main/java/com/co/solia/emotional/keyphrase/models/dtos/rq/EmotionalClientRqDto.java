package com.co.solia.emotional.keyphrase.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * entity for multiple messages for emotional processing to client.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmotionalClientRqDto {

    /**
     * the list of messages to estimate.
     */
    private List<String> messages;
}
