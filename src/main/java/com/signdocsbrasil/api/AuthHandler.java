package com.signdocsbrasil.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.signdocsbrasil.api.errors.AuthenticationException;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Handles OAuth2 token acquisition and caching for the SignDocsBrasil API.
 * Supports both client_secret and private_key_jwt (ES256) authentication modes.
 * Thread-safe via ReentrantLock.
 */
final class AuthHandler {

    private static final long TOKEN_EXPIRY_BUFFER_SECONDS = 30;
    private static final Gson GSON = new Gson();

    private final String clientId;
    private final String clientSecret;
    private final String privateKeyPem;
    private final String kid;
    private final String tokenUrl;
    private final String scopeString;
    private final java.net.http.HttpClient httpClient;

    private final ReentrantLock lock = new ReentrantLock();
    private String cachedAccessToken;
    private Instant cachedExpiresAt;

    AuthHandler(Config config) {
        this.clientId = config.getClientId();
        this.clientSecret = config.getClientSecret();
        this.privateKeyPem = config.getPrivateKey();
        this.kid = config.getKid();
        this.tokenUrl = config.getTokenUrl();
        this.scopeString = String.join(" ", config.getScopes());
        this.httpClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Returns a valid access token, fetching a new one if the cached token is expired or absent.
     * Thread-safe: concurrent callers will block while a token is being refreshed.
     *
     * @return a valid Bearer access token
     * @throws AuthenticationException if the token request fails
     */
    String getAccessToken() {
        lock.lock();
        try {
            if (cachedAccessToken != null && cachedExpiresAt != null
                    && Instant.now().isBefore(cachedExpiresAt.minusSeconds(TOKEN_EXPIRY_BUFFER_SECONDS))) {
                return cachedAccessToken;
            }
            return fetchToken();
        } finally {
            lock.unlock();
        }
    }

    private String fetchToken() {
        try {
            StringJoiner formBody = new StringJoiner("&");
            formBody.add("grant_type=" + urlEncode("client_credentials"));
            formBody.add("client_id=" + urlEncode(clientId));
            formBody.add("scope=" + urlEncode(scopeString));

            if (clientSecret != null && !clientSecret.isEmpty()) {
                formBody.add("client_secret=" + urlEncode(clientSecret));
            } else if (privateKeyPem != null && !privateKeyPem.isEmpty()) {
                String assertion = buildJwtAssertion();
                formBody.add("client_assertion_type=" + urlEncode("urn:ietf:params:oauth:client-assertion-type:jwt-bearer"));
                formBody.add("client_assertion=" + urlEncode(assertion));
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(formBody.toString()))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new AuthenticationException(
                        "Token request failed (" + response.statusCode() + "): " + response.body());
            }

            JsonObject json = GSON.fromJson(response.body(), JsonObject.class);
            String accessToken = json.get("access_token").getAsString();
            long expiresIn = json.get("expires_in").getAsLong();

            this.cachedAccessToken = accessToken;
            this.cachedExpiresAt = Instant.now().plusSeconds(expiresIn);

            return accessToken;
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Failed to acquire access token: " + e.getMessage(), e);
        }
    }

    /**
     * Builds a JWT assertion for private_key_jwt authentication using ES256.
     */
    private String buildJwtAssertion() {
        try {
            long now = Instant.now().getEpochSecond();

            // Build JWT header
            JsonObject header = new JsonObject();
            header.addProperty("alg", "ES256");
            header.addProperty("typ", "JWT");
            header.addProperty("kid", kid);

            // Build JWT payload
            JsonObject payload = new JsonObject();
            payload.addProperty("iss", clientId);
            payload.addProperty("sub", clientId);
            payload.addProperty("aud", tokenUrl);
            payload.addProperty("exp", now + 300);
            payload.addProperty("iat", now);
            payload.addProperty("jti", UUID.randomUUID().toString());

            String encodedHeader = base64UrlEncode(GSON.toJson(header).getBytes(StandardCharsets.UTF_8));
            String encodedPayload = base64UrlEncode(GSON.toJson(payload).getBytes(StandardCharsets.UTF_8));
            String signingInput = encodedHeader + "." + encodedPayload;

            // Sign with ES256 (ECDSA using P-256 and SHA-256)
            PrivateKey privateKey = parseEcPrivateKey(privateKeyPem);
            Signature signer = Signature.getInstance("SHA256withECDSA");
            signer.initSign(privateKey);
            signer.update(signingInput.getBytes(StandardCharsets.UTF_8));
            byte[] derSignature = signer.sign();

            // Convert DER-encoded ECDSA signature to fixed-length R||S format (64 bytes for P-256)
            byte[] rawSignature = derToRaw(derSignature);
            String encodedSignature = base64UrlEncode(rawSignature);

            return signingInput + "." + encodedSignature;
        } catch (Exception e) {
            throw new AuthenticationException("Failed to build JWT assertion: " + e.getMessage(), e);
        }
    }

    /**
     * Parses a PEM-encoded EC private key.
     */
    private static PrivateKey parseEcPrivateKey(String pem) throws Exception {
        String stripped = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN EC PRIVATE KEY-----", "")
                .replace("-----END EC PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(stripped);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * Converts a DER-encoded ECDSA signature to the raw R||S format required by JWS.
     * For P-256, R and S are each 32 bytes, producing a 64-byte output.
     */
    private static byte[] derToRaw(byte[] der) {
        // DER format: 0x30 [total-length] 0x02 [r-length] [r] 0x02 [s-length] [s]
        int offset = 2; // skip SEQUENCE tag and length

        // Handle multi-byte length encoding
        if ((der[1] & 0x80) != 0) {
            offset += (der[1] & 0x7F);
        }

        // Parse R
        if (der[offset] != 0x02) {
            throw new IllegalArgumentException("Invalid DER signature: expected INTEGER tag for R");
        }
        offset++;
        int rLength = der[offset++] & 0xFF;
        byte[] r = new byte[rLength];
        System.arraycopy(der, offset, r, 0, rLength);
        offset += rLength;

        // Parse S
        if (der[offset] != 0x02) {
            throw new IllegalArgumentException("Invalid DER signature: expected INTEGER tag for S");
        }
        offset++;
        int sLength = der[offset++] & 0xFF;
        byte[] s = new byte[sLength];
        System.arraycopy(der, offset, s, 0, sLength);

        // Convert to fixed-length 32-byte components
        int componentLength = 32; // P-256 uses 32-byte components
        byte[] raw = new byte[componentLength * 2];

        copyComponent(r, raw, 0, componentLength);
        copyComponent(s, raw, componentLength, componentLength);

        return raw;
    }

    /**
     * Copies a variable-length integer component into a fixed-length buffer,
     * handling leading zero padding and trimming.
     */
    private static void copyComponent(byte[] src, byte[] dest, int destOffset, int componentLength) {
        if (src.length == componentLength) {
            System.arraycopy(src, 0, dest, destOffset, componentLength);
        } else if (src.length > componentLength) {
            // Trim leading zero byte (DER adds 0x00 for positive numbers with high bit set)
            System.arraycopy(src, src.length - componentLength, dest, destOffset, componentLength);
        } else {
            // Pad with leading zeros
            System.arraycopy(src, 0, dest, destOffset + componentLength - src.length, src.length);
        }
    }

    private static String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
