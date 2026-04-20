package com.signdocsbrasil.api.tokencache;

import java.util.Optional;

/**
 * Pluggable cache for OAuth2 access tokens.
 *
 * <p>Default implementation is {@link InMemoryTokenCache}, which scopes the
 * cache to the lifetime of a single JVM. Long-lived daemons can keep using
 * the default. Stateless hosts (serverless, Lambda, short-lived CLI
 * invocations) should supply an implementation backed by a shared store
 * (Redis, Memcached, a file, etc.) to avoid fetching a fresh token on
 * every invocation.
 *
 * <p>Implementations MUST be safe to call concurrently — a {@code set}
 * that races with another {@code set} for the same key should leave the
 * cache in a consistent state. Implementations SHOULD treat the key as
 * opaque; the SDK derives keys deterministically from credentials +
 * base URL + scopes via {@link TokenCacheKeys#derive}.
 */
public interface TokenCache {

    /**
     * Retrieve a cached token for {@code key}, or {@link Optional#empty()}
     * if missing or expired. Implementations SHOULD return empty (not
     * throw) on any backend error.
     */
    Optional<CachedToken> get(String key);

    /**
     * Store {@code token} under {@code key}. Implementations SHOULD honor
     * the token's {@code expiresAt} as the storage TTL upper bound.
     */
    void set(String key, CachedToken token);

    /**
     * Remove the cached token for {@code key}. Idempotent: deleting a
     * missing entry is a no-op.
     */
    void delete(String key);
}
