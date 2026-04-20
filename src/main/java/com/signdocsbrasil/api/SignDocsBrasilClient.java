package com.signdocsbrasil.api;

import com.signdocsbrasil.api.resources.*;
import com.signdocsbrasil.api.tokencache.TokenCache;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

/**
 * Main entry point for the SignDocsBrasil API SDK.
 *
 * <p>Create an instance using the builder pattern:
 * <pre>{@code
 * // Client secret authentication
 * SignDocsBrasilClient client = SignDocsBrasilClient.builder()
 *     .clientId("your-client-id")
 *     .clientSecret("your-client-secret")
 *     .build();
 *
 * // Private key JWT authentication (ES256)
 * SignDocsBrasilClient client = SignDocsBrasilClient.builder()
 *     .clientId("your-client-id")
 *     .privateKey(pemEncodedKey)
 *     .kid("your-key-id")
 *     .build();
 *
 * // Create a transaction
 * Transaction tx = client.transactions().create(request);
 *
 * // Check health (no auth required)
 * HealthCheckResponse health = client.health().check();
 *
 * // Verify webhook signature
 * boolean valid = WebhookVerifier.verifySignature(body, sig, ts, secret);
 * }</pre>
 *
 * <p>The client is thread-safe and can be shared across threads.
 * A single instance should be reused for the lifetime of the application.
 */
public final class SignDocsBrasilClient {

    private final HealthResource health;
    private final TransactionsResource transactions;
    private final DocumentsResource documents;
    private final StepsResource steps;
    private final SigningResource signing;
    private final EvidenceResource evidence;
    private final VerificationResource verification;
    private final UsersResource users;
    private final WebhooksResource webhooks;
    private final DocumentGroupsResource documentGroups;
    private final SigningSessionsResource signingSessions;
    private final EnvelopesResource envelopes;

    private SignDocsBrasilClient(Config config) {
        config.validate();

        AuthHandler auth = new AuthHandler(config);
        HttpClient http = new HttpClient(config, auth);

        this.health = new HealthResource(http);
        this.transactions = new TransactionsResource(http);
        this.documents = new DocumentsResource(http);
        this.steps = new StepsResource(http);
        this.signing = new SigningResource(http);
        this.evidence = new EvidenceResource(http);
        this.verification = new VerificationResource(http);
        this.users = new UsersResource(http);
        this.webhooks = new WebhooksResource(http);
        this.documentGroups = new DocumentGroupsResource(http);
        this.signingSessions = new SigningSessionsResource(http);
        this.envelopes = new EnvelopesResource(http);
    }

    /**
     * Creates a new builder for constructing a {@link SignDocsBrasilClient}.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Health check operations (no authentication required).
     */
    public HealthResource health() {
        return health;
    }

    /**
     * Transaction CRUD operations.
     */
    public TransactionsResource transactions() {
        return transactions;
    }

    /**
     * Document upload, presign, confirm, and download operations.
     */
    public DocumentsResource documents() {
        return documents;
    }

    /**
     * Step start and complete operations within a transaction.
     */
    public StepsResource steps() {
        return steps;
    }

    /**
     * Digital certificate signing operations (prepare and complete).
     */
    public SigningResource signing() {
        return signing;
    }

    /**
     * Evidence retrieval operations.
     */
    public EvidenceResource evidence() {
        return evidence;
    }

    /**
     * Public verification operations (no authentication required).
     */
    public VerificationResource verification() {
        return verification;
    }

    /**
     * User enrollment operations.
     */
    public UsersResource users() {
        return users;
    }

    /**
     * Webhook management operations.
     */
    public WebhooksResource webhooks() {
        return webhooks;
    }

    /**
     * Document group operations.
     */
    public DocumentGroupsResource documentGroups() {
        return documentGroups;
    }

    /**
     * Signing session operations.
     */
    public SigningSessionsResource signingSessions() {
        return signingSessions;
    }

    /**
     * Envelope operations (multi-signer signing flows).
     */
    public EnvelopesResource envelopes() {
        return envelopes;
    }

    /**
     * Builder for constructing a {@link SignDocsBrasilClient} instance.
     */
    public static final class Builder {

        private final Config.Builder configBuilder = Config.builder();

        private Builder() {
        }

        /**
         * Sets the OAuth2 client ID (required).
         *
         * @param clientId the client ID
         * @return this builder
         */
        public Builder clientId(String clientId) {
            configBuilder.clientId(clientId);
            return this;
        }

        /**
         * Sets the OAuth2 client secret.
         * Either this or {@link #privateKey(String)} + {@link #kid(String)} is required.
         *
         * @param clientSecret the client secret
         * @return this builder
         */
        public Builder clientSecret(String clientSecret) {
            configBuilder.clientSecret(clientSecret);
            return this;
        }

        /**
         * Sets the PEM-encoded EC private key for private_key_jwt (ES256) authentication.
         * Must be used together with {@link #kid(String)}.
         *
         * @param privateKey the PEM-encoded private key
         * @return this builder
         */
        public Builder privateKey(String privateKey) {
            configBuilder.privateKey(privateKey);
            return this;
        }

        /**
         * Sets the key ID for private_key_jwt authentication.
         * Must be used together with {@link #privateKey(String)}.
         *
         * @param kid the key ID
         * @return this builder
         */
        public Builder kid(String kid) {
            configBuilder.kid(kid);
            return this;
        }

        /**
         * Sets the API base URL. Defaults to {@code https://api.signdocs.com.br}.
         *
         * @param baseUrl the base URL
         * @return this builder
         */
        public Builder baseUrl(String baseUrl) {
            configBuilder.baseUrl(baseUrl);
            return this;
        }

        /**
         * Sets the HTTP request timeout. Defaults to 30 seconds.
         *
         * @param timeout the timeout duration
         * @return this builder
         */
        public Builder timeout(Duration timeout) {
            configBuilder.timeout(timeout);
            return this;
        }

        /**
         * Sets the maximum number of retry attempts for retryable errors (429, 500, 503).
         * Defaults to 5.
         *
         * @param maxRetries the maximum number of retries
         * @return this builder
         */
        public Builder maxRetries(int maxRetries) {
            configBuilder.maxRetries(maxRetries);
            return this;
        }

        /**
         * Sets the OAuth2 scopes to request.
         *
         * @param scopes the list of scopes
         * @return this builder
         */
        public Builder scopes(List<String> scopes) {
            configBuilder.scopes(scopes);
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
            configBuilder.httpClient(httpClient);
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
            configBuilder.logger(logger);
            return this;
        }

        /**
         * Sets a pluggable {@link TokenCache} for OAuth2 access tokens.
         * When null (default), a fresh
         * {@link com.signdocsbrasil.api.tokencache.InMemoryTokenCache}
         * is created. Supply a shared-store implementation for
         * serverless / short-lived hosts that otherwise re-fetch a
         * token on every invocation.
         *
         * @param cache the token cache (may be null to use default)
         * @return this builder
         */
        public Builder tokenCache(TokenCache cache) {
            configBuilder.tokenCache(cache);
            return this;
        }

        /**
         * Registers a callback invoked once per HTTP response the SDK
         * receives. Useful for observability: surfacing
         * {@code RateLimit-*}, {@code Deprecation}/{@code Sunset}, and
         * upstream request IDs. Exceptions thrown from the callback are
         * swallowed so that an observer bug cannot break SDK requests.
         *
         * @param callback the observer (may be null to disable)
         * @return this builder
         */
        public Builder onResponse(Consumer<ResponseMetadata> callback) {
            configBuilder.onResponse(callback);
            return this;
        }

        /**
         * Builds and returns the configured {@link SignDocsBrasilClient}.
         *
         * @return the client instance
         * @throws IllegalArgumentException if the configuration is invalid
         */
        public SignDocsBrasilClient build() {
            Config config = configBuilder.build();
            return new SignDocsBrasilClient(config);
        }
    }
}
