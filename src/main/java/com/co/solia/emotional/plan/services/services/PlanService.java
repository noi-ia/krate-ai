package com.co.solia.emotional.plan.services.services;

import com.co.solia.emotional.plan.models.dtos.rq.CreatePlanRqDto;
import com.co.solia.emotional.plan.models.dtos.rq.UpdatePlanRqDto;
import com.co.solia.emotional.plan.models.dtos.rs.CreatePlanRsDto;
import com.co.solia.emotional.plan.models.dtos.rs.GetPlanRsDto;
import com.co.solia.emotional.plan.models.dtos.rs.UpdatePlanRsDto;
import com.co.solia.emotional.share.models.dtos.rs.DefaultRsDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * contract for plan services.
 * @author luis.bolivar.
 */
public interface PlanService {

    /**
     * create a new plan.
     * @param rq to create a plan.
     * @param adminCode permission to create plan.
     * @return {@link Optional} of {@link CreatePlanRsDto}.
     */
    Optional<CreatePlanRsDto> create(CreatePlanRqDto rq, String adminCode);

    /**
     * get a plan by id.
     * @param id plan identifier.
     * @return {@link Optional} of {@link GetPlanRsDto}.
     */
    Optional<GetPlanRsDto> getById(UUID id);

    /**
     * get a plan by name.
     * @param name plan name.
     * @return {@link Optional} of {@link GetPlanRsDto}.
     */
    Optional<GetPlanRsDto> getByName(String name);

    /**
     * get all plans.
     * @return {@link Optional} of {@link List} of {@link GetPlanRsDto}.
     */
    Optional<List<GetPlanRsDto>> getAll();

    /**
     * update a plan.
     * @param rq to update a plan.
     * @return {@link Optional} of {@link UpdatePlanRsDto}.
     */
    Optional<UpdatePlanRsDto>update(UUID id, UpdatePlanRqDto rq);

    /**
     * delete a plan by id.
     * @param id plan identifier.
     * @return {@link Optional} of {@link DefaultRsDto}.
     */
    Optional<DefaultRsDto>deleteById(UUID id);

    /**
     * delete a plan by name.
     * @param name plan name.
     * @return {@link Optional} of {@link DefaultRsDto}.
     */
    Optional<DefaultRsDto>deleteByName(String name);
}
