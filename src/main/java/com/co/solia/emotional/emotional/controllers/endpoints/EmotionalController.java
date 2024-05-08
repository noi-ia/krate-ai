package com.co.solia.emotional.emotional.controllers.endpoints;

import com.co.solia.emotional.emotional.controllers.docs.EmotionalControllerDocs;
import com.co.solia.emotional.emotional.controllers.validators.EmotionalValidator;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRqDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRsDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessagesRqDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessagesRsDto;
import com.co.solia.emotional.emotional.models.exceptions.InternalServerException;
import com.co.solia.emotional.emotional.models.exceptions.NotFoundException;
import com.co.solia.emotional.emotional.services.services.EmotionalService;
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
     * @return {@link ResponseEntity} of {@link EmotionalMessageRsDto}.
     */
    @PostMapping("/compute/")
    public ResponseEntity<EmotionalMessageRsDto> compute(@RequestBody final EmotionalMessageRqDto emotionalMessage) {
        EmotionalValidator.validateMessage(emotionalMessage.getMessage());
        return emotionalService.compute(emotionalMessage)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> InternalServerException.builder()
                        .message("The compute process failed, try again.")
                        .endpoint("/emotional/").build());
    }

    /**
     * process a list of messages in emotional estimation.
     * @param messages to process.
     * @return {@link ResponseEntity} of {@link EmotionalMessagesRsDto}.
     */
    @PostMapping("/compute/batch/")
    public ResponseEntity<EmotionalMessagesRsDto> computeList(@RequestBody final EmotionalMessagesRqDto messages) {
        EmotionalValidator.validateMessages(messages.getMessages());
        return emotionalService.computeList(messages)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> InternalServerException.builder()
                        .message("The compute process failed, try again.")
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
                        .message("Error getting emotional compute by id")
                        .endpoint("/emotional/{idEE}")
                        .build());
    }

    /**
     * get the messages processed by batch emotional compute id.
     * @param idBEE batch emotional estimation id.
     * @return {@link ResponseEntity} of {@link EmotionalMessagesRsDto}.
     */
    @GetMapping("/batch/{idBEE}")
    public ResponseEntity<EmotionalMessagesRsDto> getByBatchECId(@PathVariable("idBEE") final UUID idBEE){
        EmotionalValidator.validateIdEe(idBEE);
        return emotionalService.getECByBatchId(idBEE)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> NotFoundException.builder()
                        .message("Error getting emotional compute by batch id")
                        .endpoint("/emotional/{idBEE}")
                        .build());
    }
}
