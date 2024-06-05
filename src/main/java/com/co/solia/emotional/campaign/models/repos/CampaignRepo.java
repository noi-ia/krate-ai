package com.co.solia.emotional.campaign.models.repos;

import com.co.solia.emotional.campaign.models.daos.CampaignDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * campaign repository.
 *
 * @author luis.bolivar.
 */
@Repository
public interface CampaignRepo extends MongoRepository<CampaignDao, UUID> {
}
