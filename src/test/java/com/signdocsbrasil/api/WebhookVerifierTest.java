package com.signdocsbrasil.api;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class WebhookVerifierTest {

    private static String sign(String body, String secret, long timestamp) {
        try {
            String signingInput = timestamp + "." + body;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xFF));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void validSignature() {
        String body = "{\"event\":\"transaction.completed\"}";
        String secret = "whsec_test123";
        long ts = System.currentTimeMillis() / 1000;
        String sig = sign(body, secret, ts);

        assertTrue(WebhookVerifier.verifySignature(body, sig, String.valueOf(ts), secret));
    }

    @Test
    void invalidSignature() {
        long ts = System.currentTimeMillis() / 1000;
        assertFalse(WebhookVerifier.verifySignature("{}", "invalid_hex", String.valueOf(ts), "secret"));
    }

    @Test
    void expiredTimestamp() {
        String body = "{\"event\":\"test\"}";
        String secret = "whsec_test";
        long ts = System.currentTimeMillis() / 1000 - 400;
        String sig = sign(body, secret, ts);

        assertFalse(WebhookVerifier.verifySignature(body, sig, String.valueOf(ts), secret));
    }

    @Test
    void futureTimestamp() {
        String body = "{\"event\":\"test\"}";
        String secret = "whsec_test";
        long ts = System.currentTimeMillis() / 1000 + 400;
        String sig = sign(body, secret, ts);

        assertFalse(WebhookVerifier.verifySignature(body, sig, String.valueOf(ts), secret));
    }

    @Test
    void customTolerance() {
        String body = "{\"event\":\"test\"}";
        String secret = "whsec_test";
        long ts = System.currentTimeMillis() / 1000 - 100;
        String sig = sign(body, secret, ts);

        assertFalse(WebhookVerifier.verifySignature(body, sig, String.valueOf(ts), secret, 50));
        assertTrue(WebhookVerifier.verifySignature(body, sig, String.valueOf(ts), secret, 200));
    }

    @Test
    void wrongSecret() {
        String body = "{\"event\":\"test\"}";
        long ts = System.currentTimeMillis() / 1000;
        String sig = sign(body, "correct_secret", ts);

        assertFalse(WebhookVerifier.verifySignature(body, sig, String.valueOf(ts), "wrong_secret"));
    }

    @Test
    void nonNumericTimestamp() {
        assertFalse(WebhookVerifier.verifySignature("{}", "abc", "not-a-number", "secret"));
    }

    @Test
    void nullInputsReturnFalse() {
        assertFalse(WebhookVerifier.verifySignature(null, "sig", "123", "secret"));
        assertFalse(WebhookVerifier.verifySignature("{}", null, "123", "secret"));
        assertFalse(WebhookVerifier.verifySignature("{}", "sig", null, "secret"));
        assertFalse(WebhookVerifier.verifySignature("{}", "sig", "123", null));
    }
}
