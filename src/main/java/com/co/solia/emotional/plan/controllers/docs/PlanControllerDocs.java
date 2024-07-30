package com.co.solia.emotional.plan.controllers.docs;

import com.co.solia.emotional.plan.models.dtos.rq.CreatePlanRqDto;
import com.co.solia.emotional.plan.models.dtos.rq.UpdatePlanRqDto;
import com.co.solia.emotional.plan.models.dtos.rs.CreatePlanRsDto;
import com.co.solia.emotional.plan.models.dtos.rs.GetPlanRsDto;
import com.co.solia.emotional.plan.models.dtos.rs.UpdatePlanRsDto;
import com.co.solia.emotional.share.models.dtos.rs.DefaultRsDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * class to map the api documentation on the plan endpoints.
 * @author luis.bolivar.
 */
public interface PlanControllerDocs {

    /**
     * create a new plan.
     * @param rq to create a new plan.
     * @return {@link ResponseEntity} of {@link CreatePlanRsDto}.
     */
    ResponseEntity<CreatePlanRsDto> create(CreatePlanRqDto rq, String adminCode);

    /**
     * get a plan by id.
     * @param id plan identifier.
     * @return {@link ResponseEntity} of {@link GetPlanRsDto}.
     */
    ResponseEntity<GetPlanRsDto> getById(UUID id);

    /**
     * get a plan by name.
     * @param name plan name.
     * @return {@link ResponseEntity} of {@link GetPlanRsDto}.
     */
    ResponseEntity<GetPlanRsDto> getByName(String name);

    /**
     * get all plans.
     * @return {@link ResponseEntity} of {@link List} of {@link GetPlanRsDto}.
     */
    ResponseEntity<List<GetPlanRsDto>> getAll();

    /**
     * update a plan.
     * @param rq to update a plan.
     * @return {@link ResponseEntity} of {@link UpdatePlanRsDto}.
     */
    ResponseEntity<UpdatePlanRsDto>update(UUID id, UpdatePlanRqDto rq, String adminCode);

    /**
     * delete a plan by id.
     * @param id plan identifier.
     * @return {@link ResponseEntity} of {@link DefaultRsDto}.
     */
    ResponseEntity<DefaultRsDto>deleteById(UUID id, String adminCode);

    /**
     * delete a plan by name.
     * @param name plan name.
     * @return {@link ResponseEntity} of {@link DefaultRsDto}.
     */
    ResponseEntity<DefaultRsDto>deleteByName(String name, String adminCode);
}
