package com.signdocsbrasil.api.tokencache;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Default in-process {@link TokenCache}. Equivalent to the behavior the
 * SDK shipped with in 1.2.x and earlier — cache lives for the lifetime
 * of the JVM.
 *
 * <p>Thread-safe via {@link ConcurrentHashMap}.
 */
public final class InMemoryTokenCache implements TokenCache {

    private final ConcurrentMap<String, CachedToken> store = new ConcurrentHashMap<>();

    @Override
    public Optional<CachedToken> get(String key) {
        CachedToken entry = store.get(key);
        if (entry == null) {
            return Optional.empty();
        }
        // Passive eviction when we detect a hard-expired entry. Use zero
        // skew here — the caller is responsible for its own refresh window.
        if (entry.isExpired(Instant.now(), Duration.ZERO)) {
            store.remove(key, entry);
            return Optional.empty();
        }
        return Optional.of(entry);
    }

    @Override
    public void set(String key, CachedToken token) {
        store.put(key, token);
    }

    @Override
    public void delete(String key) {
        store.remove(key);
    }
}
