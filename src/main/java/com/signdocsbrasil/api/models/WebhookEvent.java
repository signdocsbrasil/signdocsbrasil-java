package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

/**
 * Canonical set of webhook event types accepted by the SignDocs API.
 *
 * <p>Stays in lockstep with the OpenAPI spec {@code WebhookEventType}
 * enum at {@code openapi/openapi.yaml}. Events tagged NT65 are emitted
 * only for tenants with {@code nt65ComplianceEnabled} (INSS consignado
 * workflow).
 */
public enum WebhookEvent {

    @SerializedName("TRANSACTION.CREATED")
    TRANSACTION_CREATED("TRANSACTION.CREATED"),

    @SerializedName("TRANSACTION.COMPLETED")
    TRANSACTION_COMPLETED("TRANSACTION.COMPLETED"),

    @SerializedName("TRANSACTION.CANCELLED")
    TRANSACTION_CANCELLED("TRANSACTION.CANCELLED"),

    @SerializedName("TRANSACTION.FAILED")
    TRANSACTION_FAILED("TRANSACTION.FAILED"),

    @SerializedName("TRANSACTION.EXPIRED")
    TRANSACTION_EXPIRED("TRANSACTION.EXPIRED"),

    @SerializedName("TRANSACTION.FALLBACK")
    TRANSACTION_FALLBACK("TRANSACTION.FALLBACK"),

    @SerializedName("TRANSACTION.DEADLINE_APPROACHING")
    TRANSACTION_DEADLINE_APPROACHING("TRANSACTION.DEADLINE_APPROACHING"),

    @SerializedName("STEP.STARTED")
    STEP_STARTED("STEP.STARTED"),

    @SerializedName("STEP.COMPLETED")
    STEP_COMPLETED("STEP.COMPLETED"),

    @SerializedName("STEP.FAILED")
    STEP_FAILED("STEP.FAILED"),

    @SerializedName("STEP.PURPOSE_DISCLOSURE_SENT")
    STEP_PURPOSE_DISCLOSURE_SENT("STEP.PURPOSE_DISCLOSURE_SENT"),

    @SerializedName("QUOTA.WARNING")
    QUOTA_WARNING("QUOTA.WARNING"),

    @SerializedName("API.DEPRECATION_NOTICE")
    API_DEPRECATION_NOTICE("API.DEPRECATION_NOTICE"),

    @SerializedName("SIGNING_SESSION.CREATED")
    SIGNING_SESSION_CREATED("SIGNING_SESSION.CREATED"),

    @SerializedName("SIGNING_SESSION.COMPLETED")
    SIGNING_SESSION_COMPLETED("SIGNING_SESSION.COMPLETED"),

    @SerializedName("SIGNING_SESSION.CANCELLED")
    SIGNING_SESSION_CANCELLED("SIGNING_SESSION.CANCELLED"),

    @SerializedName("SIGNING_SESSION.EXPIRED")
    SIGNING_SESSION_EXPIRED("SIGNING_SESSION.EXPIRED"),

    @SerializedName("ENVELOPE.CREATED")
    ENVELOPE_CREATED("ENVELOPE.CREATED"),

    @SerializedName("ENVELOPE.ALL_SIGNED")
    ENVELOPE_ALL_SIGNED("ENVELOPE.ALL_SIGNED"),

    @SerializedName("ENVELOPE.EXPIRED")
    ENVELOPE_EXPIRED("ENVELOPE.EXPIRED");

    private static final Set<WebhookEvent> NT65_EVENTS = EnumSet.of(
            TRANSACTION_DEADLINE_APPROACHING,
            STEP_PURPOSE_DISCLOSURE_SENT);

    private final String wireValue;

    WebhookEvent(String wireValue) {
        this.wireValue = wireValue;
    }

    /**
     * The wire-format string exchanged with the API (e.g.
     * {@code "TRANSACTION.CREATED"}).
     */
    public String getWireValue() {
        return wireValue;
    }

    /**
     * True if this event is part of the NT65 INSS consignado flow and
     * only emitted for tenants with {@code nt65ComplianceEnabled}.
     */
    public boolean isNt65() {
        return NT65_EVENTS.contains(this);
    }

    /**
     * Resolve an enum constant by its wire-format string. Returns
     * {@link Optional#empty()} if no constant matches.
     */
    public static Optional<WebhookEvent> fromWireValue(String wireValue) {
        if (wireValue == null) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(e -> e.wireValue.equals(wireValue))
                .findFirst();
    }
}
