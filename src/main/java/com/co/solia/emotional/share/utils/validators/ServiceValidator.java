package com.co.solia.emotional.share.utils.validators;

import com.co.solia.emotional.campaign.models.dtos.rq.CampaignRqDto;
import com.co.solia.emotional.plan.models.dtos.rq.CreatePlanRqDto;
import com.co.solia.emotional.plan.models.dtos.rq.UpdatePlanRqDto;
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
                .filter(Validator::isValidString)
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
    public static void validateId(final UUID id, final String endpoint) {
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
        Validator.isValidField(Validator.isValidId(rq.getKeyphraseId()), "keyphrase", "/campaign/");
        Validator.isValidField(Validator.isValidId(rq.getBrandId()), "brandId", "/campaign/");
        Validator.isValidField(Validator.isValidId(rq.getEmotionalId()), "emotionalId", "/campaign/");
        log.info("[validateComputeCampaign]: the data is valid to generate the campaign.");
    }

    /**
     * validate the data for create a new plan.
     * @param rq request with the data to create a new plan.
     * @param adminCode authorization to create the plan.
     */
    public static void validateCreatePlan(final CreatePlanRqDto rq, final String adminCode) {
        Validator.isValidField(Validator.isValidString(adminCode),"adminCode", "/plan/");
        Validator.isValidField(Validator.isValidString(rq.name()), "name", "/plan/");
        Validator.isValidField(Validator.isValidInteger(rq.camsByMonth()), "camsByMonth", "/plan/");
        Validator.isValidField(Validator.isValidInteger(rq.refreshByCam()), "refreshByCam", "/plan/");
        Validator.isValidField(Validator.isValidBigDecimal(rq.priceMonth()), "priceMonth", "/plan/");
        Validator.isValidField(Validator.isValidBigDecimal(rq.priceYear()), "priceYear", "/plan/");
        log.info("[validateCreatePlan]: the data is valid to generate the campaign.");
    }

    /**
     * validate the data to update a plan.
     * @param id plan identifier.
     * @param rq data to update the plan with.
     */
    public static void validateUpdatePlan(final UUID id, final UpdatePlanRqDto rq, final String adminCode) {
        Validator.isValidField(Validator.isValidString(adminCode), "adminCode", "/plan/{id}");
        Validator.isValidField(Validator.isValidId(id), "id", "/plan/{id}");
        Validator.isValidField(validatePrices(rq), "prices", "/plan/{id}");
        log.info("[validateUpdatePlan]: the data is valid to update the plan.");
    }

    /**
     * validate the prices, min one must be different to null.
     * @param rq to validate the prices.
     * @return {@link Boolean} where just one must be different to null.
     */
    private static Boolean validatePrices(final UpdatePlanRqDto rq) {
        final Boolean isValidPriceMonth = Validator.isValidBigDecimal(rq.priceMonth());
        final Boolean isValidPriceYear = Validator.isValidBigDecimal(rq.priceYear());
        return  isValidPriceMonth || isValidPriceYear;
    }
}
