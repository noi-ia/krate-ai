package com.co.solia.emotional.clean.models.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

/**
 * class to persistence the clean process.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Document("clean")
public class CleanDao {

    /**
     * id of cleaning.
     */
    @Id
    private UUID id;

    /**
     * user identifier that start cleaning.
     */
    private UUID idUser;

    /**
     * id of batch cleaning.
     */
    private UUID idBatch;

    /**
     * message associated to the cleaning.
     */
    private String message;

    /**
     * result of cleaning in String.
     */
    private String result;

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
     * date of cleaning created.
     */
    @Builder.Default
    private long created = Instant.now().getEpochSecond();

    /**
     * duration of cleaning.
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
