package com.co.solia.emotional.controllers.endpoints;

import com.co.solia.emotional.controllers.docs.EmotionalControllerDocs;
import com.co.solia.emotional.controllers.validators.EmotionalValidator;
import com.co.solia.emotional.models.dtos.EmotionalMessageRqDto;
import com.co.solia.emotional.models.dtos.EmotionalMessageRsDto;
import com.co.solia.emotional.models.exceptions.InternalServerException;
import com.co.solia.emotional.services.services.EmotionalService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * version 1 of emotional endpoints, implementation of the endpoints for documentation.
 *
 * @author luis.bolivar
 */
@RestController
@RequestMapping("/V1/emotional")
@AllArgsConstructor
public class EmotionalController implements EmotionalControllerDocs {

    /**
     * {@link EmotionalService} to use emotional behaviours.
     */
    private EmotionalService emotionalService;

    /**
     * process the message in emotional estimation.
     * @param emotionalMessage message to process.
     * @return {@link ResponseEntity} of.
     */
    @PostMapping("/")
    public ResponseEntity<EmotionalMessageRsDto> estimate(@RequestBody final EmotionalMessageRqDto emotionalMessage) {
        EmotionalValidator.validateMessage(emotionalMessage.getMessage());
        return emotionalService.estimate(emotionalMessage)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> InternalServerException.builder()
                        .message("The estimation process failed, try again.")
                        .endpoint("/emotional/").build());
    }

    /**
     * get emotional estimation by id.
     * @param idEE to get emotional estimation.
     * @return {@link ResponseEntity} of {@link EmotionalMessageRsDto}
     */
    @GetMapping("/{idEE}")
    public ResponseEntity<EmotionalMessageRsDto> getById(@PathVariable("idEE") final UUID idEE) {
        EmotionalValidator.validateIdEe(idEE);
        return emotionalService.getById(idEE)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> InternalServerException.builder()
                        .message("Error getting emotional estimation by id")
                        .endpoint("/emotional/{id}")
                        .build());
    }
}
