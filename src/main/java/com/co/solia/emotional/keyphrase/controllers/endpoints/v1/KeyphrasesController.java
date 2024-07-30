package com.co.solia.emotional.keyphrase.controllers.endpoints.v1;

import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphraseRsDto;
import com.co.solia.emotional.share.models.exceptions.NotFoundException;
import com.co.solia.emotional.share.utils.validators.ServiceValidator;
import com.co.solia.emotional.keyphrase.controllers.docs.KeyphraseControllerDocs;
import com.co.solia.emotional.keyphrase.models.dtos.rq.KeyphraseRqDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.KeyphrasesRsDto;
import com.co.solia.emotional.keyphrase.models.enums.EmotionEnum;
import com.co.solia.emotional.keyphrase.services.services.KeyphraseService;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
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
 * endpoints of keyphrase.
 *
 * @author luis.bolivar.
 */
@RestController
@RequestMapping("/1/keyphrase")
@Slf4j
@AllArgsConstructor
public class KeyphrasesController implements KeyphraseControllerDocs {

    /**
     * dependency on {@link KeyphraseService}.
     */
    private KeyphraseService keyphraseService;

    /**
     * {@inheritDoc}.
     *
     * @param keyphraseRq
     * @param emotion
     * @return
     */
    @PostMapping("/compute/{emotion}")
    public ResponseEntity<KeyphrasesRsDto> compute(@RequestBody final KeyphraseRqDto keyphraseRq,
                                                   @PathVariable("emotion") final EmotionEnum emotion) {
        ServiceValidator.validateMessages(keyphraseRq.getMessages(), "/keyphrase/compute/{emotion}");
        return keyphraseService.compute(keyphraseRq, emotion)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[compute]: Error processing the messages.");
                    throw InternalServerException.builder()
                            .message("Error processing the messages.")
                            .endpoint("/1/keyphrase/compute/")
                            .build();
                });
    }

    /**
     * get a keyphrase by id.
     *
     * @param id {@link UUID} to get the keyphrase.
     * @return {@link ResponseEntity} of {@link KeyphrasesRsDto}.
     */
    @GetMapping("/compute/{id}")
    public ResponseEntity<KeyphrasesRsDto> getById(@PathVariable("id") final UUID id) {
        ServiceValidator.validateId(id, "/keyphrase/compute/{id}");
        return keyphraseService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[getById]: Error processing the messages.");
                    throw NotFoundException.builder()
                            .message("Error keyphrase not found..")
                            .endpoint("/keyphrase/compute/{id}")
                            .build();
                });
    }

    /**
     * get a keyphrase by id.
     *
     * @param id {@link UUID} to get the keyphrase.
     * @return {@link ResponseEntity} of {@link KeyphrasesRsDto}.
     */
    @GetMapping("/compute/keyphrase/{id}")
    public ResponseEntity<KeyphraseRsDto> getKeyphraseById(@PathVariable("id") final UUID id) {
        ServiceValidator.validateId(id, "/keyphrase/compute/keyphrase/{id}");
        return keyphraseService.getKeyphraseById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[getKeyphraseById]: Error processing the messages.");
                    throw NotFoundException.builder()
                            .message("Error keyphrase not found..")
                            .endpoint("/keyphrase/compute/keyphrase/{id}")
                            .build();
                });
    }
}
