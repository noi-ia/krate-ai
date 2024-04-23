package com.co.solia.emotional.emotional.models.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

/**
 * representation of emotional estimation for the db.
 *
 * @author luis.bolivar.
 */
@Builder
@Getter
@Document("emotionalEstimation")
@AllArgsConstructor
@NoArgsConstructor
public class EmotionalEstimationDao {

    /**
     * id of emotional estimation.
     */
    @Id
    private UUID idEE;

    /**
     * id of batch emotional estimation.
     */
    private UUID idBEE;

    /**
     * user identifier that start emotional estimation.
     */
    private UUID idUser;

    /**
     * message associated to the emotional estimation.
     */
    private String message;

    /**
     * result of emotional estimation in json format.
     */
    private String estimates;

    /**
     * tokens of message representing.
     */
    private Integer tokens;


    /**
     * is active this processing to still reviewing.
     */
    @Builder.Default
    private Boolean activate = Boolean.TRUE;

    /**
     * date of emotional estimation created.
     */
    @Builder.Default
    private long created = Instant.now().getEpochSecond();

    /**
     * duration of emotional estimation.
     */
    private long duration;

    /**
     * processing id from openai.
     */
    private String openAiId;

    /**
     * system fingerprint from openai result.
     */
    private String fingerPrintOpenai;
}
