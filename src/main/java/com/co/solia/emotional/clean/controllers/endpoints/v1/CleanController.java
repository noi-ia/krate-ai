package com.co.solia.emotional.clean.controllers.endpoints.v1;

import com.co.solia.emotional.clean.controllers.docs.CleanControllerDocs;
import com.co.solia.emotional.clean.models.dtos.rq.CleanRqDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanRsDto;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.clean.services.services.CleanService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
