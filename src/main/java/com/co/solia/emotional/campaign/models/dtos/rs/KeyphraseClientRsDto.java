package com.co.solia.emotional.campaign.models.dtos.rs;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * keyphrase response from client.
 * @param id of the keyphrase.
 * @param keyphraseId of the process to generate the keyphrase.
 * @param keyphrase string plain.
 * @author luis.bolivar.
 */
@Builder
public record KeyphraseClientRsDto(
        UUID id,
        UUID keyphraseId,
        String keyphrase
){
}
