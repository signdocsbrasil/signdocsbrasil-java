package com.signdocsbrasil.api;

import com.signdocsbrasil.api.tokencache.TokenCache;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Configuration for the SignDocsBrasil API client.
 * Use the {@link Builder} to construct instances.
 */
public final class Config {

    public static final String DEFAULT_BASE_URL = "https://api.signdocs.com.br";
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    public static final int DEFAULT_MAX_RETRIES = 5;
    public static final List<String> DEFAULT_SCOPES = Collections.unmodifiableList(Arrays.asList(
            "transactions:read",
            "transactions:write",
            "steps:write",
            "evidence:read",
            "webhooks:write"
    ));

    private final String clientId;
    private final String clientSecret;
    private final String privateKey;
    private final String kid;
    private final String baseUrl;
    private final Duration timeout;
    private final int maxRetries;
    private final List<String> scopes;
    private final java.net.http.HttpClient httpClient;
    private final java.util.logging.Logger logger;
    private final TokenCache tokenCache;
    private final Consumer<ResponseMetadata> onResponse;

    private Config(Builder builder) {
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.privateKey = builder.privateKey;
        this.kid = builder.kid;
        this.baseUrl = builder.baseUrl != null ? builder.baseUrl : DEFAULT_BASE_URL;
        this.timeout = builder.timeout != null ? builder.timeout : DEFAULT_TIMEOUT;
        this.maxRetries = builder.maxRetries != null ? builder.maxRetries : DEFAULT_MAX_RETRIES;
        this.scopes = builder.scopes != null
                ? Collections.unmodifiableList(builder.scopes)
                : DEFAULT_SCOPES;
        this.httpClient = builder.httpClient;
        this.logger = builder.logger;
        this.tokenCache = builder.tokenCache;
        this.onResponse = builder.onResponse;
    }

    /**
     * Validates this configuration and throws if invalid.
     *
     * @throws IllegalArgumentException if required fields are missing or inconsistent
     */
    void validate() {
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("clientId is required");
        }
        if ((clientSecret == null || clientSecret.isEmpty())
                && (privateKey == null || privateKey.isEmpty())) {
            throw new IllegalArgumentException("Either clientSecret or privateKey+kid is required");
        }
        if (privateKey != null && !privateKey.isEmpty()
                && (kid == null || kid.isEmpty())) {
            throw new IllegalArgumentException("kid is required when using privateKey");
        }
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getKid() {
        return kid;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public List<String> getScopes() {
        return scopes;
    }

    /**
     * Returns the custom HTTP client, or null if the default should be used.
     */
    public java.net.http.HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Returns the logger, or null if logging is disabled.
     */
    public java.util.logging.Logger getLogger() {
        return logger;
    }

    /**
     * Returns the custom token cache, or null if the default in-memory
     * cache should be used.
     */
    public TokenCache getTokenCache() {
        return tokenCache;
    }

    /**
     * Returns the response-metadata callback, or null if no observer is
     * configured.
     */
    public Consumer<ResponseMetadata> getOnResponse() {
        return onResponse;
    }

    public String getTokenUrl() {
        return baseUrl + "/oauth2/token";
    }

    /**
     * Returns true if this configuration uses client_secret authentication.
     */
    public boolean usesClientSecret() {
        return clientSecret != null && !clientSecret.isEmpty();
    }

    /**
     * Returns true if this configuration uses private_key_jwt authentication.
     */
    public boolean usesPrivateKeyJwt() {
        return privateKey != null && !privateKey.isEmpty();
    }

    /**
     * Creates a new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link Config}.
     */
    public static final class Builder {
        private String clientId;
        private String clientSecret;
        private String privateKey;
        private String kid;
        private String baseUrl;
        private Duration timeout;
        private Integer maxRetries;
        private List<String> scopes;
        private java.net.http.HttpClient httpClient;
        private java.util.logging.Logger logger;
        private TokenCache tokenCache;
        private Consumer<ResponseMetadata> onResponse;

        private Builder() {
        }

        /**
         * Sets the OAuth2 client ID (required).
         */
        public Builder clientId(String clientId) {
            this.clientId = Objects.requireNonNull(clientId, "clientId must not be null");
            return this;
        }

        /**
         * Sets the OAuth2 client secret. Either this or privateKey+kid must be provided.
         */
        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        /**
         * Sets the PEM-encoded EC private key for private_key_jwt authentication.
         * Must be used together with {@link #kid(String)}.
         */
        public Builder privateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        /**
         * Sets the key ID (kid) for private_key_jwt authentication.
         * Must be used together with {@link #privateKey(String)}.
         */
        public Builder kid(String kid) {
            this.kid = kid;
            return this;
        }

        /**
         * Sets the API base URL. Defaults to {@value Config#DEFAULT_BASE_URL}.
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * Sets the HTTP request timeout. Defaults to 30 seconds.
         */
        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Sets the maximum number of retries for retryable errors. Defaults to {@value Config#DEFAULT_MAX_RETRIES}.
         */
        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        /**
         * Sets the OAuth2 scopes to request.
         */
        public Builder scopes(List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        /**
         * Sets a custom {@link java.net.http.HttpClient} to use for API requests.
         * When provided, the SDK will use this client instead of building its own.
         *
         * @param httpClient the custom HTTP client
         * @return this builder
         */
        public Builder httpClient(java.net.http.HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * Sets a {@link java.util.logging.Logger} for request/response logging.
         * When null (default), no logging is performed.
         *
         * @param logger the logger instance
         * @return this builder
         */
        public Builder logger(java.util.logging.Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * Sets a pluggable {@link TokenCache} for OAuth2 access tokens.
         * When null (default), a fresh {@link com.signdocsbrasil.api.tokencache.InMemoryTokenCache}
         * is created. Supply a shared-store implementation (Redis,
         * Memcached, etc.) for serverless / short-lived hosts that
         * otherwise re-fetch a token on every invocation.
         *
         * @param cache the token cache (may be null to use default)
         * @return this builder
         */
        public Builder tokenCache(TokenCache cache) {
            this.tokenCache = cache;
            return this;
        }

        /**
         * Registers a callback invoked once per HTTP response the SDK
         * receives (including both successful and error responses).
         * Useful for observability: surfacing {@code RateLimit-*},
         * {@code Deprecation}/{@code Sunset}, and upstream request IDs.
         *
         * <p>Exceptions thrown from the callback are swallowed so that an
         * observer bug cannot break SDK requests. They are logged at
         * {@link java.util.logging.Level#WARNING} when a logger is
         * configured.
         *
         * @param callback the observer (may be null to disable)
         * @return this builder
         */
        public Builder onResponse(Consumer<ResponseMetadata> callback) {
            this.onResponse = callback;
            return this;
        }

        /**
         * Builds and validates the configuration.
         *
         * @return the validated Config
         * @throws IllegalArgumentException if the configuration is invalid
         */
        public Config build() {
            Config config = new Config(this);
            config.validate();
            return config;
        }
    }
}
