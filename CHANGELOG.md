# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
