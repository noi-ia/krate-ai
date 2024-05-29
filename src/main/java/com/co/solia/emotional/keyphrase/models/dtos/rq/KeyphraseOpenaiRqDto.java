package com.co.solia.emotional.keyphrase.models.dtos.rq;

import com.co.solia.emotional.keyphrase.models.enums.EmotionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * dto to send the data to openai with the prompt.
 *
 * @author luis.bolivar.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KeyphraseOpenaiRqDto {

    /**
     * emotion to generate the keyphrases.
     */
    private EmotionEnum emotion;

    /**
     * messages to get the keyphrases.
     */
    private List<String> messages;

    /**
     * emotions associated with the messages.
     */
    private Map<String, Double> emotions;
}
