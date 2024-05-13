package com.co.solia.emotional.emotional.models.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Entity to save in db the batch emotional process-
 *
 * @author luis.bolivar
 */
@Builder
@Getter
@Document("emotionalUnique")
@AllArgsConstructor
@NoArgsConstructor
public class EmotionalUniqueDao {

    /**
     * identifier.
     */
    @Id
    private UUID id;

    /**
     * message processed.
     */
    private List<String> messages;

    /**
     * result of emotional processing.
     */
    private String estimations;

    /**
     * user identifier that start emotional estimation.
     */
    private UUID idUser;

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
