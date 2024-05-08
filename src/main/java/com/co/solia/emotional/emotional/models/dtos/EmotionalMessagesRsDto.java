package com.co.solia.emotional.emotional.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * class to map the result of multiple messages processed from emotional api.
 *
 * @author luis.bolivar
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class EmotionalMessagesRsDto {

    /**
     * id of emotional estimation batch process.
     */
    private UUID idBee;

    /**
     * results from emotional processing.
     */
    private List<EmotionalMessageRsDto> results;
}
