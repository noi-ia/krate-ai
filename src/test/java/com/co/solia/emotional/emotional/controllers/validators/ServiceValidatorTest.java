package com.co.solia.emotional.emotional.controllers.validators;

import com.co.solia.emotional.share.models.validators.ServiceValidator;
import com.co.solia.emotional.share.models.exceptions.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

/**
 * test for {@link ServiceValidator}.
 *
 * @author luis.bolivar
 */
@ExtendWith(MockitoExtension.class)
public class ServiceValidatorTest {

    /**
     * happy path, validate message.
     */
    @Test
    void givenMessageWhenValidateMessageThenValidateOk(){
        final String message = "I'm happy.";
        ServiceValidator.validateMessage(message, "val");
        Assertions.assertTrue(Boolean.TRUE, "The message was successfully validated.");
    }

    /**
     * not happy path, validate message null.
     */
    @Test
    void givenMessageNullWhenValidateMessageThenValidateOk(){
        final String message = null;
        final BadRequestException bre = Assertions.assertThrows(BadRequestException.class,
                () -> ServiceValidator.validateMessage(message, "val"));
        Assertions.assertNotNull(bre, "the exception is not null.");
        Assertions.assertNotNull(bre.getMessage(), "the exception message is not null.");
        Assertions.assertNotNull(bre.getEndpoint(), "the exception endpoint is not null.");
        Assertions.assertEquals("the message to process is invalid.", bre.getMessage(), "the message is the same.");
        Assertions.assertEquals("/emotional/", bre.getEndpoint(), "the endpoint is the same.");
    }

    /**
     * not happy path, validate message blank.
     */
    @Test
    void givenMessageBlankWhenValidateMessageThenValidateOk(){
        final String message = "    ";
        final BadRequestException bre = Assertions.assertThrows(BadRequestException.class,
                () -> ServiceValidator.validateMessage(message, "val"));
        Assertions.assertNotNull(bre, "the exception is not null.");
        Assertions.assertNotNull(bre.getMessage(), "the exception message is not null.");
        Assertions.assertNotNull(bre.getEndpoint(), "the exception endpoint is not null.");
        Assertions.assertEquals("the message to process is invalid.", bre.getMessage(), "the message is the same.");
        Assertions.assertEquals("/emotional/", bre.getEndpoint(), "the endpoint is the same.");
    }

    /**
     * not happy path, validate message empty.
     */
    @Test
    void givenMessageEmptyWhenValidateMessageThenValidateOk(){
        final String message = "";
        final BadRequestException bre = Assertions.assertThrows(BadRequestException.class,
                () -> ServiceValidator.validateMessage(message, "val"));
        Assertions.assertNotNull(bre, "the exception is not null.");
        Assertions.assertNotNull(bre.getMessage(), "the exception message is not null.");
        Assertions.assertNotNull(bre.getEndpoint(), "the exception endpoint is not null.");
        Assertions.assertEquals("the message to process is invalid.", bre.getMessage(), "the message is the same.");
        Assertions.assertEquals("/emotional/", bre.getEndpoint(), "the endpoint is the same.");
    }

    /**
     * happy path, validate id ee.
     */
    @Test
    void givenIdEeWhenValidateIdThenValidateOk(){
        final UUID idEe = UUID.randomUUID();
        ServiceValidator.validateId(idEe, "val");
        Assertions.assertTrue(Boolean.TRUE, "The endpoint is valid");
    }

    /**
     * not happy path, validate id null.
     */
    @Test
    void givenIdNullEeWhenValidateIdThenValidateOk(){
        final UUID idEe = null;
        final BadRequestException bre = Assertions.assertThrows(BadRequestException.class,
                () -> ServiceValidator.validateId(idEe, "val"));
        Assertions.assertNotNull(bre, "the exception is not null.");
        Assertions.assertNotNull(bre.getMessage(), "the exception message is not null.");
        Assertions.assertNotNull(bre.getEndpoint(), "the exception endpoint is not null.");
        Assertions.assertEquals("the id to process is invalid.", bre.getMessage(), "the message is the same.");
        Assertions.assertEquals("/emotional/{id}", bre.getEndpoint(), "the endpoint is the same.");
    }
}
