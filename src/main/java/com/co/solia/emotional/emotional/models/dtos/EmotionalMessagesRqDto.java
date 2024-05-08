package com.co.solia.emotional.emotional.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * entity for multiple messages for emotional processing.
 *
 * @author luis.bolivar
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmotionalMessagesRqDto {
    private List<String> messages;
}
