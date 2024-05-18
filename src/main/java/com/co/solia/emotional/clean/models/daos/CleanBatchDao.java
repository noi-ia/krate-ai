package com.co.solia.emotional.clean.models.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity to save in db the batch cleaning process.
 *
 * @author luis.bolivar
 */
@Builder
@Getter
@Document("cleanBatch")
@AllArgsConstructor
@NoArgsConstructor
public class CleanBatchDao {

    /**
     * id of batch emotional compute.
     */
    @Id
    private UUID id;

    /**
     * amount of messages to process.
     */
    private Integer amountMessages;

    /**
     * user identifier.
     */
    private UUID userId;

    /**
     * date of emotional estimation created.
     */
    @Builder.Default
    private long created = Instant.now().getEpochSecond();

    /**
     * duration of emotional estimation.
     */
    private long duration;
}
