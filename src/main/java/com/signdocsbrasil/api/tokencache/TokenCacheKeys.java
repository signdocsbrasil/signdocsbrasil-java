package com.signdocsbrasil.api.tokencache;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helpers for deriving stable, opaque cache keys from credential +
 * base URL + scope material.
 *
 * <p>Keys are hashed (SHA-256, truncated to 32 hex chars) so that a
 * leaked cache key cannot be reversed to recover the client ID.
 */
public final class TokenCacheKeys {

    private static final String PREFIX = "signdocs.oauth.";
    private static final int HEX_LENGTH = 32;

    private TokenCacheKeys() {
    }

    /**
     * Derive a deterministic cache key from canonical credential material.
     *
     * <p>Scopes are sorted before hashing so that callers don't have to
     * care about ordering. Trailing slashes on {@code baseUrl} are
     * stripped so that {@code https://api.signdocs.com.br} and
     * {@code https://api.signdocs.com.br/} yield the same key.
     *
     * @param clientId OAuth2 client ID
     * @param baseUrl  API base URL (trailing slash is ignored)
     * @param scopes   OAuth2 scopes; ordering is canonicalized
     * @return cache key of the form {@code signdocs.oauth.<32 hex chars>}
     */
    public static String derive(String clientId, String baseUrl, List<String> scopes) {
        String normalizedBaseUrl = stripTrailingSlash(baseUrl == null ? "" : baseUrl);
        List<String> canonicalScopes = scopes == null ? Collections.emptyList() : new ArrayList<>(scopes);
        Collections.sort(canonicalScopes);
        String material = (clientId == null ? "" : clientId)
                + "|" + normalizedBaseUrl
                + "|" + String.join(" ", canonicalScopes);

        return PREFIX + sha256Hex(material).substring(0, HEX_LENGTH);
    }

    private static String stripTrailingSlash(String s) {
        int end = s.length();
        while (end > 0 && s.charAt(end - 1) == '/') {
            end--;
        }
        return s.substring(0, end);
    }

    private static String sha256Hex(String material) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(material.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(Character.forDigit((b >> 4) & 0xF, 16));
                sb.append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is required by every JRE; this should never happen.
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
