package com.co.solia.emotional.campaign.models.daos;

import com.co.solia.emotional.campaign.models.dtos.dtos.PillarDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("keyphrase")
@Getter
public class CampaignDao {

    /**
     * identifier.
     */
    @Id
    private UUID id;

    /**
     * name of the campaign.
     */
    private String name;

    /**
     * description of the campaign.
     */
    private String description;

    /**
     * pillars of the campaign.
     */
    private List<PillarDto> pillars;

    /**
     * keyphrase associated with the campaign.
     */
    private String keyphrase;

    /**
     * emotions id for emotions processing.
     */
    private UUID emotionsId;

    /**
     * user identifier that start emotional estimation.
     */
    private UUID idUser;

    /**
     * tokens of message representing.
     */
    private Map<String, Integer> tokens;

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
     * brand identifier associated.
     */
    private UUID brandId;

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
