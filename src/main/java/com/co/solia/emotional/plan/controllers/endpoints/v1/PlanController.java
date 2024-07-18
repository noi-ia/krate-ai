package com.co.solia.emotional.plan.controllers.endpoints.v1;

import com.co.solia.emotional.plan.controllers.docs.PlanControllerDocs;
import com.co.solia.emotional.plan.models.dtos.rq.CreatePlanRqDto;
import com.co.solia.emotional.plan.models.dtos.rq.UpdatePlanRqDto;
import com.co.solia.emotional.plan.models.dtos.rs.CreatePlanRsDto;
import com.co.solia.emotional.plan.models.dtos.rs.GetPlanRsDto;
import com.co.solia.emotional.plan.models.dtos.rs.UpdatePlanRsDto;
import com.co.solia.emotional.plan.services.services.PlanService;
import com.co.solia.emotional.share.models.dtos.rs.DefaultRsDto;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.models.exceptions.NotFoundException;
import com.co.solia.emotional.share.models.validators.ServiceValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * controller for all services for plan resource.
 * @author luis.bolivar.
 */
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/1/plan")
public class PlanController implements PlanControllerDocs {

    /**
     * dependency on {@link PlanService}.
     */
    private PlanService planService;

    /**
     * create a new plan.
     *
     * @param rq to create a new plan.
     * @return {@link ResponseEntity} of {@link CreatePlanRsDto}.
     */
    @Override
    @PostMapping("/")
    public ResponseEntity<CreatePlanRsDto> create(@RequestBody final CreatePlanRqDto rq,
                                                  @RequestHeader("x-a-x") final String adminCode) {
        ServiceValidator.validateCreatePlan(rq, adminCode);
        return planService.create(rq, adminCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[create]: error creating the plan, please try again.");
                    throw InternalServerException.builder()
                            .endpoint("/plan/")
                            .message("error creating the plan, please try again.")
                            .build();
                });
    }

    /**
     * get a plan by id.
     *
     * @param id plan identifier.
     * @return {@link ResponseEntity} of {@link GetPlanRsDto}.
     */
    @Override
    @GetMapping("/id/{id}")
    public ResponseEntity<GetPlanRsDto> getById(@PathVariable("id") final UUID id) {
        ServiceValidator.validateId(id, "/plan/{id}");
        return planService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[getById]: error getting the plan by id {}, please try again.", id);
                    throw NotFoundException.builder()
                            .endpoint("/plan/")
                            .message("error getting the plan by id %s, please try again.".formatted(id))
                            .build();
                });
    }

    /**
     * get a plan by name.
     *
     * @param name plan name.
     * @return {@link ResponseEntity} of {@link GetPlanRsDto}.
     */
    @Override
    @GetMapping("/name/{name}")
    public ResponseEntity<GetPlanRsDto> getByName(@PathVariable("name") final String name) {
        ServiceValidator.validateMessage(name, "/plan/{name}");
        return planService.getByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[getByName]: error getting the plan by name {}, please try again.", name);
                    throw NotFoundException.builder()
                            .endpoint("/plan/")
                            .message("error getting the plan by name %s, please try again.".formatted(name))
                            .build();
                });
    }

    /**
     * get all plans.
     *
     * @return {@link ResponseEntity} of {@link List} of {@link GetPlanRsDto}.
     */
    @Override
    @GetMapping("/")
    public ResponseEntity<List<GetPlanRsDto>> getAll() {
        return planService.getAll()
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[getAll]: error getting all plans please try again.");
                    throw NotFoundException.builder()
                            .endpoint("/plan/")
                            .message("error getting all plans please try again.")
                            .build();
                });
    }

    /**
     * update a plan.
     *
     * @param rq to update a plan.
     * @param id
     * @return {@link ResponseEntity} of {@link UpdatePlanRsDto}.
     */
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<UpdatePlanRsDto> update(
            @PathVariable("id") final UUID id,
            @RequestBody final UpdatePlanRqDto rq) {
        ServiceValidator.validateUpdatePlan(id, rq);
        return planService.update(id, rq)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[update]: error updating the plan: {} please try again.", id);
                    throw InternalServerException.builder()
                            .endpoint("/plan/")
                            .message("error updating the plan: %s please try again".formatted(id))
                            .build();
                });
    }

    /**
     * delete a plan by id.
     *
     * @param id plan identifier.
     * @return {@link ResponseEntity} of {@link DefaultRsDto}.
     */
    @Override
    public ResponseEntity<DefaultRsDto> deleteById(UUID id) {
        return null;
    }

    /**
     * delete a plan by name.
     *
     * @param name plan name.
     * @return {@link ResponseEntity} of {@link DefaultRsDto}.
     */
    @Override
    public ResponseEntity<DefaultRsDto> deleteByName(String name) {
        return null;
    }
}
