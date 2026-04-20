package com.signdocsbrasil.api.tokencache;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Immutable value object representing a cached OAuth2 access token
 * along with its absolute expiry instant.
 *
 * <p>Uses a final class rather than a Java 14+ record because the SDK
 * targets Java 11.
 */
public final class CachedToken {

    private final String accessToken;
    private final Instant expiresAt;

    public CachedToken(String accessToken, Instant expiresAt) {
        this.accessToken = Objects.requireNonNull(accessToken, "accessToken must not be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt must not be null");
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    /**
     * True if {@code now} is at or past {@code expiresAt - skew}.
     *
     * @param now  reference instant (usually {@link Instant#now()})
     * @param skew safety window to refresh the token before it actually
     *             expires. Pass {@link Duration#ZERO} for an exact check.
     */
    public boolean isExpired(Instant now, Duration skew) {
        Objects.requireNonNull(now, "now must not be null");
        Objects.requireNonNull(skew, "skew must not be null");
        return !now.isBefore(expiresAt.minus(skew));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CachedToken)) return false;
        CachedToken that = (CachedToken) o;
        return accessToken.equals(that.accessToken) && expiresAt.equals(that.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, expiresAt);
    }
}
