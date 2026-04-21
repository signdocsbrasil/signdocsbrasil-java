# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.3.0] - 2026-04-20

### Fixed

- `WebhooksResource.list()` now correctly returns `List<Webhook>`. Previously Gson tried to deserialize the API's `{"webhooks":[...],"count":N}` envelope into `List<Webhook>` and threw `JsonSyntaxException: Expected BEGIN_OBJECT but was BEGIN_ARRAY`. The method now deserializes into an internal `WebhookListEnvelope` type and returns its inner list.

### Added

- `com.signdocsbrasil.api.tokencache.TokenCache` — pluggable OAuth token cache interface. Inject via `SignDocsBrasilClient.builder().tokenCache(myCache)` to share tokens across serverless / short-lived workers. Default `InMemoryTokenCache` (backed by `ConcurrentHashMap`) preserves pre-1.3 single-JVM behavior.
- `com.signdocsbrasil.api.tokencache.CachedToken` immutable value object and `com.signdocsbrasil.api.tokencache.InMemoryTokenCache` default implementation.
- `com.signdocsbrasil.api.tokencache.TokenCacheKeys.derive(clientId, baseUrl, scopes)` — deterministic SHA-256-based cache keys (`signdocs.oauth.<32 hex>`) so the same credentials reuse the same cached token across process boundaries. Trailing slashes on `baseUrl` are normalized and scopes are canonicalized before hashing.
- `com.signdocsbrasil.api.ResponseMetadata` — captures `RateLimit-*`, `Deprecation`, `Sunset`, and request-ID (`X-Request-Id` / `X-SignDocs-Request-Id`) headers from every API response. Register an observer via `SignDocsBrasilClient.builder().onResponse(callback)`. RFC 8594 `Deprecation`/`Sunset` parsing accepts both IMF-fixdate (`DateTimeFormatter.RFC_1123_DATE_TIME`) and `@<unix-seconds>` forms.
- `com.signdocsbrasil.api.models.WebhookEvent` — Java enum with all 17 canonical event types from the OpenAPI spec `WebhookEventType`, including Gson `@SerializedName` annotations for wire-format (de)serialization and an `isNt65()` predicate.
- Webhook event types for the NT65 INSS consignado flow:
  - `STEP.PURPOSE_DISCLOSURE_SENT` — purpose-disclosure notification delivered to the beneficiary
  - `TRANSACTION.DEADLINE_APPROACHING` — ≤2 business days remaining until the INSS submission deadline
- `Config.Builder#tokenCache(TokenCache)` and `Config.Builder#onResponse(Consumer<ResponseMetadata>)` setters, wired through `SignDocsBrasilClient.Builder` for the same ergonomics as the rest of the client config.

### Changed

- `com.signdocsbrasil.api.AuthHandler` is no longer `final`. Subclassing is supported; prefer injecting a `TokenCache` over subclassing for most use cases.
- `AuthHandler#getAccessToken()` now reads from and writes to the configured `TokenCache`. Cache keys are derived deterministically via `TokenCacheKeys.derive(clientId, baseUrl, scopes)`.
- `AuthHandler#invalidate()` (new) deletes the cache entry for the current credentials.
- Token expiry check now uses a `Duration`-based 30-second skew via `CachedToken#isExpired(Instant, Duration)` rather than an internal long constant.
- `HttpClient` `User-Agent` bumped to `signdocs-brasil-java/1.3.0` (previously stuck at `1.0.0`). The SDK version constant now tracks the Maven `<version>` again.
- `HttpClient` invokes the optional `onResponse` observer after every response, wrapped in `try/catch` — observer exceptions are swallowed and logged via the configured `java.util.logging.Logger`, falling back to `System.Logger`.

### Deprecated

- None.

### Fixed

- `SDK_VERSION` constant in `HttpClient` was stale at `1.0.0` across 1.1.x and 1.2.x releases; now matches the Maven version.

## [1.2.0] - 2026-04-14

### Added

- `client.verification().verifyEnvelope(envelopeId)` — public method for the new `GET /v1/verify/envelope/{envelopeId}` endpoint. Returns envelope status, signers list (each with `evidenceId` for drill-down via `verification().verify()`), and consolidated download URLs.
- `EnvelopeVerificationResponse` model with nested `EnvelopeSigner` and `Downloads` types. For non-PDF envelopes signed with digital certificates, `downloads.getConsolidatedSignature()` exposes a single PKCS#7 / CMS detached `.p7s` containing every signer's `SignerInfo`. For PDF envelopes, `downloads.getCombinedSignedPdf()` exposes the merged PDF.
- `VerificationResponse.VerificationSigner.getCpfCnpj()` and `VerificationResponse.getTenantCnpj()` getters (previously returned by the API but not modeled by the SDK).
- `VerificationDownloadsResponse.Downloads.getOriginalDocument()` and `getSignedSignature()` getters (previously undocumented), matching the real shape the API returns.

### Changed

- `VerificationDownloadsResponse.Downloads.getSignedSignature()` returns `null` when the evidence belongs to a multi-signer envelope (the API omits the field). For standalone signing sessions (single-signer non-PDF with digital certificate) the field is still populated. To retrieve the consolidated `.p7s` for an envelope, use `client.verification().verifyEnvelope()` instead.

### Fixed

- README install instructions pointed at `com.signdocsbrasil:signdocsbrasil-api` (wrong groupId that does not resolve). The actual Maven Central coordinates are `io.github.signdocsbrasil:signdocsbrasil-api`. README now matches `pom.xml`.

### Removed

- `VerificationDownloadsResponse.Downloads.getSignedPdf()` — the field was modeled by the SDK but never actually returned by the API. No real-world consumer could have depended on it.

## [1.1.0] - 2026-03-27

### Added

- Envelopes resource (`client.envelopes()`): create, get, addSession, combinedStamp — multi-signer workflows with parallel or sequential signing
- New models: CreateEnvelopeRequest, Envelope, AddEnvelopeSessionRequest, EnvelopeSession, EnvelopeSessionSummary, EnvelopeDetail, EnvelopeCombinedStampResponse

## [1.0.0] - 2026-03-02

### Added

- Full API coverage: transactions, documents, steps, signing, evidence, verification, users, webhooks, documentGroups, health
- OAuth2 `client_credentials` authentication with client secret
- Private Key JWT (ES256) authentication with `client_assertion`
- Automatic token caching with 30-second refresh buffer
- Thread-safe token refresh via `ReentrantLock`
- Auto-pagination via `listAutoPaginate()` returning `Iterable<Transaction>`
- Exponential backoff retry with jitter (429, 500, 503)
- Retry-After header support
- Idempotency keys (auto-generated UUID) on POST requests
- Typed exceptions for all HTTP error codes (RFC 7807 ProblemDetail)
- Webhook signature verification (HMAC-SHA256, constant-time comparison)
- Configurable base URL, timeout, max retries, and scopes
- Builder pattern for client configuration
- Comprehensive Javadoc documentation
- Minimal dependency: Gson 2.11+ only
- Java 11+ support
