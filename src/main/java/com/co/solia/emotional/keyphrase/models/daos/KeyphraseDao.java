package com.co.solia.emotional.keyphrase.models.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Entity to save in db the keyphrase process.
 *
 * @author luis.bolivar
 */
@Builder
@Getter
@Document("keyphrase")
@AllArgsConstructor
@NoArgsConstructor
public class KeyphraseDao {
    /**
     * identifier.
     */
    @Id
    private UUID id;

    /**
     * keyphrases process identifier.
     */
    private UUID idKeyphrases;

    /**
     * a keyphrase.
     */
    private String keyphrase;
}
