package com.signdocsbrasil.api;

import com.signdocsbrasil.api.models.WebhookEvent;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class WebhookEventTest {

    /**
     * Canonical 17 event types from openapi/openapi.yaml:2473
     * (WebhookEventType schema). Keep this list in lockstep with the spec.
     */
    private static final List<String> SPEC_EVENTS = Arrays.asList(
            "TRANSACTION.CREATED",
            "TRANSACTION.COMPLETED",
            "TRANSACTION.CANCELLED",
            "TRANSACTION.FAILED",
            "TRANSACTION.EXPIRED",
            "TRANSACTION.FALLBACK",
            "TRANSACTION.DEADLINE_APPROACHING",
            "STEP.STARTED",
            "STEP.COMPLETED",
            "STEP.FAILED",
            "STEP.PURPOSE_DISCLOSURE_SENT",
            "QUOTA.WARNING",
            "API.DEPRECATION_NOTICE",
            "SIGNING_SESSION.CREATED",
            "SIGNING_SESSION.COMPLETED",
            "SIGNING_SESSION.CANCELLED",
            "SIGNING_SESSION.EXPIRED");

    @Test
    void enumCoversAllSpecEvents() {
        Set<String> enumWireValues = Arrays.stream(WebhookEvent.values())
                .map(WebhookEvent::getWireValue)
                .collect(Collectors.toSet());
        Set<String> expected = new HashSet<>(SPEC_EVENTS);

        assertEquals(expected, enumWireValues,
                "WebhookEvent enum must match the OpenAPI spec WebhookEventType");
        assertEquals(17, WebhookEvent.values().length);
    }

    @Test
    void fromWireValueRoundTrips() {
        for (String wire : SPEC_EVENTS) {
            Optional<WebhookEvent> found = WebhookEvent.fromWireValue(wire);
            assertTrue(found.isPresent(), "missing: " + wire);
            assertEquals(wire, found.get().getWireValue());
        }
    }

    @Test
    void fromWireValueUnknownIsEmpty() {
        assertFalse(WebhookEvent.fromWireValue("BOGUS.EVENT").isPresent());
        assertFalse(WebhookEvent.fromWireValue(null).isPresent());
    }

    @Test
    void isNt65FlagsExactlyTheTwoNt65Events() {
        Set<WebhookEvent> nt65 = Arrays.stream(WebhookEvent.values())
                .filter(WebhookEvent::isNt65)
                .collect(Collectors.toSet());
        assertEquals(
                Set.of(
                        WebhookEvent.STEP_PURPOSE_DISCLOSURE_SENT,
                        WebhookEvent.TRANSACTION_DEADLINE_APPROACHING),
                nt65);
    }

    @Test
    void newNt65EventsArePresent() {
        // Smoke-test: explicit guard against accidentally dropping either
        // NT65 event during a future refactor.
        assertEquals("STEP.PURPOSE_DISCLOSURE_SENT",
                WebhookEvent.STEP_PURPOSE_DISCLOSURE_SENT.getWireValue());
        assertEquals("TRANSACTION.DEADLINE_APPROACHING",
                WebhookEvent.TRANSACTION_DEADLINE_APPROACHING.getWireValue());
    }
}
