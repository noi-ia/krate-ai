package com.co.solia.emotional.keyphrase.models.dtos.rs;

import com.co.solia.emotional.keyphrase.models.enums.EmotionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

/**
 * response to generate the keyphrase.
 *
 * @author luis.bolivar.
 */
@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeyphrasesRsDto {

    /**
     * id of the keyphrase generation.
     */
    private UUID id;

    /**
     * emotion associated to the keyphrase.
     */
    private EmotionEnum emotion;

    /**
     * messages.
     */
    private List<String> messages;

    /**
     * emotions related to the messages.
     */
    private EmotionalDto emotions;

    /**
     * list resulting to get the keyphrases.
     */
    private List<KeyphraseRsDto> keyphrases;
}
