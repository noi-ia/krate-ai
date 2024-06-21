package com.co.solia.emotional.campaign.models.mappers;

import com.co.solia.emotional.share.models.dtos.rs.EmotionalClientRsDto;
import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.Optional;

/**
 * class to map the all responses from emotional api.
 *
 * @author luis.bolivar.
 */
@UtilityClass
@Slf4j
public class EmotionalMapper {

    /**
     * get a {@link EmotionalClientRsDto} from a {@link Response}
     * @param rs to get the {@link EmotionalClientRsDto}.
     * @return an {@link EmotionalClientRsDto}.
     */
    public static Optional<EmotionalClientRsDto> getFromRs(final Response rs){
        Optional<EmotionalClientRsDto> result = Optional.empty();
        try {
            final EmotionalClientRsDto emotionalRs =
                    new Gson().fromJson(rs.body().string(), EmotionalClientRsDto.class);
            result = emotionalRs != null && !emotionalRs.getEmotions().isEmpty() ?
                    Optional.of(emotionalRs) : Optional.empty();
            log.info("[getFromRs]: data getting ok.");
        } catch(Exception e) {
            log.error("[getFromRs] error parsing the response: {}", e.getMessage());
        }
        return result;
    }
}
