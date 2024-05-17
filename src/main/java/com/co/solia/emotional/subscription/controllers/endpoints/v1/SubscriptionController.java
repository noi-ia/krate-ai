package com.co.solia.emotional.subscription.controllers.endpoints.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * endpoints to handle te subscriptions.
 */
@RestController
@RequestMapping("/1/subscriptions")
@Slf4j
public class SubscriptionController {

    @PostMapping("/validate/{apikey}")
    public ResponseEntity<Object> validate(@PathVariable("apikey") final String apikey) {
        log.info("[validate] validating: {}", apikey);
        return ResponseEntity.ok().build();
    }
}
