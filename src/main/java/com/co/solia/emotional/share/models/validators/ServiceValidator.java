package com.co.solia.emotional.share.models.validators;

import com.co.solia.emotional.campaign.models.dtos.rq.CampaignRqDto;
import com.co.solia.emotional.share.models.exceptions.BadRequestException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * utility class to validate the request and response.
 *
 * @author luis.bolivar
 */
@UtilityClass
@Slf4j
public class ServiceValidator {
    /**
     * method to validate the message to be processed by emotional estimation.
     * @param message to validate.
     */
    public static void validateMessage(final String message, final String endpoint) {
        Stream.of(message)
                .filter(BasicValidator::isValidString)
                .findFirst()
                .ifPresentOrElse(validMessage ->
                                log.info("[validateMessage]: message is valid to process.")
                        , () -> {
                            log.error("[validateMessage]: the message to process is invalid.");
                            throw BadRequestException.builder()
                                    .message("the message to process is invalid.")
                                    .endpoint(endpoint)
                                    .build();
                        });
    }

    /**
     * validate the id of emotional estimation.
     * @param id estimation id.
     */
    public static void validateId(final UUID id, final String endpoint){
        Stream.of(id)
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresentOrElse(idEe -> log.info("[validateId]: id of emotional estimation format is ok")
                        , () -> {
                            log.error("[validateId]: the id of emotional estimation is invalid.");
                            throw BadRequestException.builder()
                                    .message("the id to process is invalid.")
                                    .endpoint(endpoint + id)
                                    .build();
                        });
    }

    /**
     * validate the messages to compute.
     * @param messages to compute.
     */
    public static void validateMessages(final List<String> messages, final String endpoint) {
        Stream.of(messages)
                .filter(Objects::nonNull)
                .filter(m -> !m.isEmpty())
                .filter(m -> m.getFirst() != null)
                .filter(m -> !m.getFirst().isEmpty())
                .filter(m -> !m.getFirst().isBlank())
                .findFirst()
                .ifPresentOrElse(m -> log.info("[validateMessages]: messages to compute are ok"),
                        () -> {
                            log.error("[validateMessages]: Error in messages, validate them.");
                            throw BadRequestException.builder()
                                    .message("the message to compute are invalid.")
                                    .endpoint(endpoint)
                                    .build();
                        });
    }

    /**
     * validate the campaign request to generate the campaign.
     * @param rq to validate.
     */
    public static void validateCampaignRq(final CampaignRqDto rq) {
        BasicValidator.isValidField(BasicValidator.isValidId(rq.getKeyphraseId()), "keyphrase");
        BasicValidator.isValidField(BasicValidator.isValidId(rq.getBrandId()), "brandId");
        BasicValidator.isValidField(BasicValidator.isValidId(rq.getEmotionalId()), "emotionalId");
        log.info("[validateComputeCampaign]: the data is valid to generate the campaign.");
    }
}
