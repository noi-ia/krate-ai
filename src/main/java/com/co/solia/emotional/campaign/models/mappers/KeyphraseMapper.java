package com.co.solia.emotional.campaign.models.mappers;

import com.co.solia.emotional.campaign.models.dtos.rs.BrandClientRsDto;
import com.co.solia.emotional.campaign.models.dtos.rs.KeyphraseClientRsDto;
import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.Optional;

/**
 * utility class to map the keyphrase in campaign.
 *
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class KeyphraseMapper {

    /**
     * get a keyphrase from a {@link Response}
     * @param rs to get the keyphrase.
     * @return {@link Optional} of {@link KeyphraseClientRsDto}.
     */
    public static Optional<KeyphraseClientRsDto> getFromRs(final Response rs) {
        Optional<KeyphraseClientRsDto> result = Optional.empty();
        try {
            final KeyphraseClientRsDto keyphrase = new Gson().fromJson(rs.body().string(), KeyphraseClientRsDto.class);
            result = keyphrase != null && keyphrase.id() != null ? Optional.of(keyphrase) : Optional.empty();
            log.info("[getFromRs]: data getting ok.");
        } catch(Exception e) {
            log.error("[getFromRs] error parsing the response: {}", e.getMessage());
        }
        return result;
    }
}
