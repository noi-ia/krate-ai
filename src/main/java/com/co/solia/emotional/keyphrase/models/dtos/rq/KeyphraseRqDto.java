package com.co.solia.emotional.keyphrase.models.dtos.rq;

import io.swagger.v3.oas.annotations.media.Schema;
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
     * list of messages.
     */
    @Schema(
            name = "messages",
            description = "messages to generate the keyphrase estimation.",
            type = "List")
    private List<String> messages;
}
