package com.signdocsbrasil.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Utility for verifying webhook signatures.
 * Uses HMAC-SHA256 with constant-time comparison to prevent timing attacks.
 */
public final class WebhookVerifier {

    /** Default tolerance for timestamp validation: 5 minutes. */
    public static final int DEFAULT_TOLERANCE_SECONDS = 300;

    private WebhookVerifier() {
        // Static utility class
    }

    /**
     * Verifies a webhook signature with the default tolerance of 5 minutes.
     *
     * @param body            the raw request body as a string
     * @param signatureHeader the value of the X-Signature header
     * @param timestampHeader the value of the X-Timestamp header (Unix epoch seconds)
     * @param secret          the webhook signing secret
     * @return true if the signature is valid and the timestamp is within tolerance
     */
    public static boolean verifySignature(String body, String signatureHeader,
                                           String timestampHeader, String secret) {
        return verifySignature(body, signatureHeader, timestampHeader, secret, DEFAULT_TOLERANCE_SECONDS);
    }

    /**
     * Verifies a webhook signature with a custom tolerance.
     *
     * @param body              the raw request body as a string
     * @param signatureHeader   the value of the X-Signature header (hex-encoded HMAC)
     * @param timestampHeader   the value of the X-Timestamp header (Unix epoch seconds)
     * @param secret            the webhook signing secret
     * @param toleranceSeconds  the maximum allowed age of the timestamp in seconds
     * @return true if the signature is valid and the timestamp is within tolerance
     */
    public static boolean verifySignature(String body, String signatureHeader,
                                           String timestampHeader, String secret,
                                           int toleranceSeconds) {
        if (body == null || signatureHeader == null || timestampHeader == null || secret == null) {
            return false;
        }

        // Parse and validate timestamp
        long timestamp;
        try {
            timestamp = Long.parseLong(timestampHeader);
        } catch (NumberFormatException e) {
            return false;
        }

        long now = System.currentTimeMillis() / 1000;
        if (Math.abs(now - timestamp) > toleranceSeconds) {
            return false;
        }

        // Compute expected signature
        String signingInput = timestamp + "." + body;
        String expected = computeHmacSha256Hex(secret, signingInput);
        if (expected == null) {
            return false;
        }

        // Constant-time comparison
        return MessageDigest.isEqual(
                signatureHeader.getBytes(StandardCharsets.UTF_8),
                expected.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Computes HMAC-SHA256 and returns the result as a lowercase hex string.
     */
    private static String computeHmacSha256Hex(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }
}
