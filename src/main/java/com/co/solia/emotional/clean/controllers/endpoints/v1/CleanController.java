package com.co.solia.emotional.clean.controllers.endpoints.v1;

import com.co.solia.emotional.clean.controllers.docs.CleanControllerDocs;
import com.co.solia.emotional.clean.models.dtos.rq.CleanBatchRqDto;
import com.co.solia.emotional.clean.models.dtos.rq.CleanRqDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanBatchRsDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanRsDto;
import com.co.solia.emotional.share.utils.validators.ServiceValidator;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.clean.services.services.CleanService;
import com.co.solia.emotional.share.models.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * endpoint dedicated to clean data.
 *
 * @author luis.bolivar.
 */
@Slf4j
@RestController()
@RequestMapping("/1/clean")
@AllArgsConstructor
public class CleanController implements CleanControllerDocs {

    /**
     * dependency on {@link CleanService}.
     */
    private CleanService cleanService;

    /**
     * clean message.
     * @param cleanRq request with the message to clean.
     * @return {@link ResponseEntity} of {@link CleanRsDto}.
     */
    @PostMapping("/compute/")
    public ResponseEntity<CleanRsDto> clean(@RequestBody final CleanRqDto cleanRq) {
        ServiceValidator.validateMessage(cleanRq.getMessage(), "/1/clean/compute/");
        return cleanService.clean(cleanRq)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[clean]: Error cleaning message.");
                    throw InternalServerException.builder()
                            .message("Error cleaning message.")
                            .endpoint("/1/clean/compute/")
                            .build();
                });
    }

    /**
     * get {@link CleanRsDto} by id.
     * @param id cleaning identifier.
     * @return {@link ResponseEntity} of {@link CleanRsDto}.
     */
    @GetMapping("/compute/{id}")
    public ResponseEntity<CleanRsDto> getById(@PathVariable("id") final UUID id) {
        ServiceValidator.validateId(id, "/1/clean/compute/");
        return cleanService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[getById]: not found clean id: {}.", id);
                    throw NotFoundException.builder()
                            .message("not found clean id.")
                            .endpoint("/1/clean/compute/" + id)
                            .build();
                });
    }

    /**
     * clean messages.
     * @param cleanRq request with the message to clean.
     * @return {@link ResponseEntity} of {@link CleanBatchRsDto}.
     */
    @PostMapping("/compute/batch/")
    public ResponseEntity<CleanBatchRsDto> cleanList(@RequestBody final CleanBatchRqDto cleanRq) {
        ServiceValidator.validateMessages(cleanRq.getMessages(), "/1/clean/compute/batch");
        return cleanService.cleanList(cleanRq)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[cleanList]: Error cleaning message.");
                    throw InternalServerException.builder()
                            .message("Error cleaning message.")
                            .endpoint("/1/clean/compute/batch")
                            .build();
                });
    }

    /**
     * get {@link CleanBatchRsDto} by id.
     * @param id cleaning batch identifier.
     * @return {@link ResponseEntity} of {@link CleanBatchRsDto}.
     */
    @GetMapping("/compute/batch/{id}")
    public ResponseEntity<CleanBatchRsDto> getByBatchId(@PathVariable("id") final UUID id) {
        ServiceValidator.validateId(id, "/1/clean/compute/");
        return cleanService.getByBatchId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[getByBatchId]: not found clean batch id: {}.", id);
                    throw NotFoundException.builder()
                            .message("not found clean batch id.")
                            .endpoint("/1/clean/compute/batch/" + id)
                            .build();
                });
    }
}
