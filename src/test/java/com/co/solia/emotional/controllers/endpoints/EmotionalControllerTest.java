package com.co.solia.emotional.controllers.endpoints;

import com.co.solia.emotional.emotional.controllers.endpoints.EmotionalController;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRqDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionalMessageRsDto;
import com.co.solia.emotional.emotional.models.dtos.EmotionsDto;
import com.co.solia.emotional.emotional.models.exceptions.BadRequestException;
import com.co.solia.emotional.emotional.models.exceptions.InternalServerException;
import com.co.solia.emotional.emotional.services.impl.EmotionalServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * class to test the {@link EmotionalController}
 *
 * @author luis.bolivar
 */
@ExtendWith(MockitoExtension.class)
public class EmotionalControllerTest {

    /**
     * class to inject the mocks.
     */
    @InjectMocks
    private EmotionalController emotionalController;

    /**
     * mock of the {@link EmotionalServiceImpl}.
     */
    @Mock
    private EmotionalServiceImpl emotionalService;

    /**
     * happy path.
     */
    @Test
    void givenAMessageWhenEstimateThenEstimateOk(){
        final String message = "I'm so happy";
        final UUID idEe = UUID.randomUUID();
        final EmotionalMessageRqDto rq = EmotionalMessageRqDto.builder()
                .message(message)
                .build();

        final EmotionalMessageRsDto expected = EmotionalMessageRsDto.builder()
                .eeId(idEe)
                .emotions(EmotionsDto.builder().build())
                .build();

        Mockito.when(emotionalService.estimate(Mockito.any()))
                .thenReturn(Optional.of(expected));

        final ResponseEntity<EmotionalMessageRsDto> result = emotionalController.estimate(rq);

        Mockito.verify(emotionalService, Mockito.times(1))
                .estimate(Mockito.any());
        Assertions.assertNotNull(result, "The result is not null.");
        Assertions.assertNotNull(result.getBody(), "The result body is not null.");
        Assertions.assertTrue(result.getStatusCode().is2xxSuccessful(), "Is 2xxSuccessful.");
        Assertions.assertEquals(200, result.getStatusCode().value(), "Is 2xxSuccessful code.");
        Assertions.assertNotNull(result.getBody().getEeId(), "The result id is not null.");
        Assertions.assertNotNull(result.getBody().getEmotions(), "The result emotions is not null.");
        Assertions.assertEquals(idEe, result.getBody().getEeId(), "Is the same id.");
    }

    /**
     * not happy path.
     */
    @Test
    void givenAMessageWhenEstimateThenEstimateNok(){
        final String message = "I'm so happy";
        final EmotionalMessageRqDto rq = EmotionalMessageRqDto.builder()
                .message(message)
                .build();

        Mockito.when(emotionalService.estimate(Mockito.any()))
                .thenReturn(Optional.empty());

        final InternalServerException ise = Assertions.assertThrows(InternalServerException.class,
                () -> emotionalController.estimate(rq));

        Mockito.verify(emotionalService, Mockito.times(1))
                .estimate(Mockito.any());
        Assertions.assertNotNull(ise, "The result is not null.");
        Assertions.assertNotNull(ise.getMessage(), "The result body is not null.");
        Assertions.assertNotNull(ise.getEndpoint(), "Is not null.");
        Assertions.assertEquals("The estimation process failed, try again.", ise.getMessage(), "The same message.");
        Assertions.assertEquals("/emotional/", ise.getEndpoint(), "The same endpoint.");
    }

    /**
     * not happy path, with message null.
     */
    @Test
    void givenAMessageNullWhenEstimateThenEstimateNok(){
        final String message = null; // message null.
        final EmotionalMessageRqDto rq = EmotionalMessageRqDto.builder()
                .message(message)
                .build();

        final BadRequestException bre = Assertions.assertThrows(BadRequestException.class,
                () -> emotionalController.estimate(rq));

        Mockito.verify(emotionalService, Mockito.times(0))
                .estimate(Mockito.any());
        Assertions.assertNotNull(bre, "The result is not null.");
        Assertions.assertNotNull(bre.getMessage(), "The result body is not null.");
        Assertions.assertNotNull(bre.getEndpoint(), "Is not null.");
        Assertions.assertEquals("the message to process is invalid.", bre.getMessage(), "The same message.");
        Assertions.assertEquals("/emotional/", bre.getEndpoint(), "The same endpoint.");
    }

    /**
     * not happy path, with message empty.
     */
    @Test
    void givenAMessageEmptyWhenEstimateThenEstimateNok(){
        final String message = ""; // message empty.
        final EmotionalMessageRqDto rq = EmotionalMessageRqDto.builder()
                .message(message)
                .build();

        final BadRequestException bre = Assertions.assertThrows(BadRequestException.class,
                () -> emotionalController.estimate(rq));

        Mockito.verify(emotionalService, Mockito.times(0))
                .estimate(Mockito.any());
        Assertions.assertNotNull(bre, "The result is not null.");
        Assertions.assertNotNull(bre.getMessage(), "The result body is not null.");
        Assertions.assertNotNull(bre.getEndpoint(), "Is not null.");
        Assertions.assertEquals("the message to process is invalid.", bre.getMessage(), "The same message.");
        Assertions.assertEquals("/emotional/", bre.getEndpoint(), "The same endpoint.");
    }

    /**
     * not happy path, with message blank.
     */
    @Test
    void givenAMessageBlankWhenEstimateThenEstimateNok(){
        final String message = "     "; // message blank.
        final EmotionalMessageRqDto rq = EmotionalMessageRqDto.builder()
                .message(message)
                .build();

        final BadRequestException bre = Assertions.assertThrows(BadRequestException.class,
                () -> emotionalController.estimate(rq));

        Mockito.verify(emotionalService, Mockito.times(0))
                .estimate(Mockito.any());
        Assertions.assertNotNull(bre, "The result is not null.");
        Assertions.assertNotNull(bre.getMessage(), "The result body is not null.");
        Assertions.assertNotNull(bre.getEndpoint(), "Is not null.");
        Assertions.assertEquals("the message to process is invalid.", bre.getMessage(), "The same message.");
        Assertions.assertEquals("/emotional/", bre.getEndpoint(), "The same endpoint.");
    }

    /**
     * happy path get ee by id.
     */
    @Test
    void givenEeIdWhenGetByIdThenGetEeOk() {
        final UUID eeId = UUID.randomUUID();

        final EmotionalMessageRsDto expected = EmotionalMessageRsDto.builder()
                .eeId(eeId)
                .emotions(EmotionsDto.builder().build())
                .build();

        Mockito.when(emotionalService.getById(Mockito.any()))
                .thenReturn(Optional.of(expected));

        ResponseEntity<EmotionalMessageRsDto> result = emotionalController.getById(eeId);
        Mockito.verify(emotionalService, Mockito.times(1))
                .getById(Mockito.any());
        Assertions.assertNotNull(result, "The result is not null.");
        Assertions.assertNotNull(result.getBody(), "The result body is not null.");
        Assertions.assertTrue(result.getStatusCode().is2xxSuccessful(), "Is 2xxSuccessful.");
        Assertions.assertEquals(200, result.getStatusCode().value(), "Is 2xxSuccessful code.");
        Assertions.assertNotNull(result.getBody().getEeId(), "The result id is not null.");
        Assertions.assertNotNull(result.getBody().getEmotions(), "The result emotions is not null.");
        Assertions.assertEquals(eeId, result.getBody().getEeId(), "Is the same id.");
    }

    /**
     * not happy path get ee by id.
     */
    @Test
    void givenEeIdWhenGetByIdThenGetEeNok() {
        final UUID eeId = UUID.randomUUID();

        Mockito.when(emotionalService.getById(Mockito.any()))
                .thenReturn(Optional.empty());

        final InternalServerException ise = Assertions.assertThrows(InternalServerException.class,
                () -> emotionalController.getById(eeId));

        Mockito.verify(emotionalService, Mockito.times(1))
                .getById(Mockito.any());
        Assertions.assertNotNull(ise, "The result is not null.");
        Assertions.assertNotNull(ise.getMessage(), "The result body is not null.");
        Assertions.assertNotNull(ise.getEndpoint(), "Is not null.");
        Assertions.assertEquals("Error getting emotional estimation by id", ise.getMessage(), "The same message.");
        Assertions.assertEquals("/emotional/{id}", ise.getEndpoint(), "The same endpoint.");
    }

    /**
     * not happy path get ee by id, with id null
     */
    @Test
    void givenNullEeIdWhenGetByIdThenGetEeNok() {
        final UUID eeId = null; // with id null.

        final BadRequestException bre = Assertions.assertThrows(BadRequestException.class,
                () -> emotionalController.getById(eeId));

        Mockito.verify(emotionalService, Mockito.times(0))
                .getById(Mockito.any());
        Assertions.assertNotNull(bre, "The result is not null.");
        Assertions.assertNotNull(bre.getMessage(), "The result body is not null.");
        Assertions.assertNotNull(bre.getEndpoint(), "Is not null.");
        Assertions.assertEquals("the id to process is invalid.", bre.getMessage(), "The same message.");
        Assertions.assertEquals("/emotional/{id}", bre.getEndpoint(), "The same endpoint.");
    }

}
