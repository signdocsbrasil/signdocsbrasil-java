package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.AdvanceSessionRequest;
import com.signdocsbrasil.api.models.AdvanceSessionResponse;
import com.signdocsbrasil.api.models.CancelSigningSessionResponse;
import com.signdocsbrasil.api.models.CreateSigningSessionRequest;
import com.signdocsbrasil.api.models.MintSigningLinkResponse;
import com.signdocsbrasil.api.models.ResendOtpRequest;
import com.signdocsbrasil.api.models.SigningSession;
import com.signdocsbrasil.api.models.SigningSessionBootstrap;
import com.signdocsbrasil.api.models.SigningSessionListParams;
import com.signdocsbrasil.api.models.SigningSessionListResponse;
import com.signdocsbrasil.api.models.SigningSessionStatus;

import java.time.Duration;
import java.util.Collections;

/**
 * Resource for signing session operations.
 */
public final class SigningSessionsResource {

    private final HttpClient http;

    public SigningSessionsResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Creates a new signing session with an automatically generated idempotency key.
     *
     * @param request the signing session creation request
     * @return the created signing session
     */
    public SigningSession create(CreateSigningSessionRequest request) {
        return create(request, (String) null);
    }

    /**
     * Creates a new signing session with a specified idempotency key.
     *
     * @param request        the signing session creation request
     * @param idempotencyKey the idempotency key, or null to auto-generate
     * @return the created signing session
     */
    public SigningSession create(CreateSigningSessionRequest request, String idempotencyKey) {
        return http.requestWithIdempotency("POST", "/v1/signing-sessions", request,
                SigningSession.class, idempotencyKey);
    }

    /**
     * Creates a new signing session with a per-request timeout.
     *
     * @param request the signing session creation request
     * @param timeout the request timeout
     * @return the created signing session
     */
    public SigningSession create(CreateSigningSessionRequest request, Duration timeout) {
        return http.requestWithIdempotency("POST", "/v1/signing-sessions", request,
                SigningSession.class, null, timeout);
    }

    /**
     * Creates a new signing session with a specified idempotency key and per-request timeout.
     *
     * @param request        the signing session creation request
     * @param idempotencyKey the idempotency key, or null to auto-generate
     * @param timeout        the request timeout
     * @return the created signing session
     */
    public SigningSession create(CreateSigningSessionRequest request, String idempotencyKey, Duration timeout) {
        return http.requestWithIdempotency("POST", "/v1/signing-sessions", request,
                SigningSession.class, idempotencyKey, timeout);
    }

    /**
     * Retrieves the status of a signing session.
     *
     * @param sessionId the signing session ID
     * @return the signing session status
     */
    public SigningSessionStatus getStatus(String sessionId) {
        return http.request("GET", "/v1/signing-sessions/" + sessionId + "/status",
                null, SigningSessionStatus.class);
    }

    /**
     * Retrieves the status of a signing session with a per-request timeout.
     *
     * @param sessionId the signing session ID
     * @param timeout   the request timeout
     * @return the signing session status
     */
    public SigningSessionStatus getStatus(String sessionId, Duration timeout) {
        return http.request("GET", "/v1/signing-sessions/" + sessionId + "/status",
                null, SigningSessionStatus.class, timeout);
    }

    /**
     * Cancels a signing session.
     *
     * @param sessionId the signing session ID to cancel
     * @return the cancel signing session response
     */
    public CancelSigningSessionResponse cancel(String sessionId) {
        return http.request("POST", "/v1/signing-sessions/" + sessionId + "/cancel",
                null, CancelSigningSessionResponse.class);
    }

    /**
     * Cancels a signing session with a per-request timeout.
     *
     * @param sessionId the signing session ID to cancel
     * @param timeout   the request timeout
     * @return the cancel signing session response
     */
    public CancelSigningSessionResponse cancel(String sessionId, Duration timeout) {
        return http.request("POST", "/v1/signing-sessions/" + sessionId + "/cancel",
                null, CancelSigningSessionResponse.class, timeout);
    }

    /**
     * Mints a fresh signing URL for an existing session and returns it, instead of
     * e-mailing it.
     *
     * <p>A signing link is single-use: once the signer finishes — or the embed token
     * is otherwise consumed — reopening the same URL returns
     * {@code 401 Embed token has been consumed}. This issues a new one without
     * creating another transaction and without consuming quota. It works for
     * standalone and envelope sessions alike.
     *
     * <p>The session must be {@code ACTIVE}; a completed or cancelled one throws
     * {@link com.signdocsbrasil.api.errors.ConflictException}, since a link to a
     * finished session would authenticate nothing. Use
     * {@code EnvelopesResource.combinedStamp} or {@code TransactionsResource.download}
     * to reach the signed document instead.
     *
     * <p>{@code expiresAt} is inherited from the original session and is not extended.
     *
     * <p><strong>Authorises the tenant, not the end user.</strong> The API cannot tell
     * which of your users is entitled to this link, so an application whose users share
     * one tenant must establish that itself before calling — otherwise this becomes a
     * way for one user to obtain another's signing credential.
     *
     * @param sessionId the signing session ID; must be ACTIVE
     * @return the newly minted signing link
     */
    public MintSigningLinkResponse link(String sessionId) {
        return http.request("POST", "/v1/signing-sessions/" + sessionId + "/link",
                null, MintSigningLinkResponse.class);
    }

    /**
     * Mints a fresh signing URL for an existing session, with a per-request timeout.
     *
     * @param sessionId the signing session ID; must be ACTIVE
     * @param timeout   the request timeout
     * @return the newly minted signing link
     * @see #link(String)
     */
    public MintSigningLinkResponse link(String sessionId, Duration timeout) {
        return http.request("POST", "/v1/signing-sessions/" + sessionId + "/link",
                null, MintSigningLinkResponse.class, timeout);
    }

    /**
     * Gets the full bootstrap data for a signing session.
     *
     * @param sessionId the signing session ID
     * @return the signing session bootstrap data
     */
    public SigningSessionBootstrap get(String sessionId) {
        return http.request("GET", "/v1/signing-sessions/" + sessionId,
                null, SigningSessionBootstrap.class);
    }

    /**
     * Gets the full bootstrap data for a signing session with a per-request timeout.
     *
     * @param sessionId the signing session ID
     * @param timeout   the request timeout
     * @return the signing session bootstrap data
     */
    public SigningSessionBootstrap get(String sessionId, Duration timeout) {
        return http.request("GET", "/v1/signing-sessions/" + sessionId,
                null, SigningSessionBootstrap.class, timeout);
    }

    /**
     * Advances a signing session through its steps.
     *
     * @param sessionId the signing session ID
     * @param request   the advance request with action and optional parameters
     * @return the advance session response
     */
    public AdvanceSessionResponse advance(String sessionId, AdvanceSessionRequest request) {
        return http.request("POST", "/v1/signing-sessions/" + sessionId + "/advance",
                request, AdvanceSessionResponse.class);
    }

    /**
     * Advances a signing session with a per-request timeout.
     *
     * @param sessionId the signing session ID
     * @param request   the advance request
     * @param timeout   the request timeout
     * @return the advance session response
     */
    public AdvanceSessionResponse advance(String sessionId, AdvanceSessionRequest request, Duration timeout) {
        return http.request("POST", "/v1/signing-sessions/" + sessionId + "/advance",
                request, AdvanceSessionResponse.class, timeout);
    }

    /**
     * Resends the OTP challenge for a signing session.
     *
     * @param sessionId the signing session ID
     * @return the advance session response with updated step info
     */
    public AdvanceSessionResponse resendOtp(String sessionId) {
        return resendOtp(sessionId, (ResendOtpRequest) null);
    }

    /**
     * Resends the OTP challenge for a signing session, optionally selecting the
     * delivery channel.
     *
     * @param sessionId the signing session ID
     * @param request   the resend request with the desired channel, or null for the default
     * @return the advance session response with updated step info
     */
    public AdvanceSessionResponse resendOtp(String sessionId, ResendOtpRequest request) {
        return http.request("POST", "/v1/signing-sessions/" + sessionId + "/resend-otp",
                request, AdvanceSessionResponse.class);
    }

    /**
     * Resends the OTP challenge with a per-request timeout.
     *
     * @param sessionId the signing session ID
     * @param timeout   the request timeout
     * @return the advance session response with updated step info
     */
    public AdvanceSessionResponse resendOtp(String sessionId, Duration timeout) {
        return resendOtp(sessionId, (ResendOtpRequest) null, timeout);
    }

    /**
     * Resends the OTP challenge with a selected delivery channel and a per-request timeout.
     *
     * @param sessionId the signing session ID
     * @param request   the resend request with the desired channel, or null for the default
     * @param timeout   the request timeout
     * @return the advance session response with updated step info
     */
    public AdvanceSessionResponse resendOtp(String sessionId, ResendOtpRequest request, Duration timeout) {
        return http.request("POST", "/v1/signing-sessions/" + sessionId + "/resend-otp",
                request, AdvanceSessionResponse.class, timeout);
    }

    /**
     * Lists signing sessions with default parameters (no filters).
     *
     * @return the signing session list response with pagination
     */
    public SigningSessionListResponse list() {
        return list(null);
    }

    /**
     * Lists signing sessions with the given filter and pagination parameters.
     *
     * @param params the list parameters (status, limit, nextToken, etc.)
     * @return the signing session list response with pagination
     */
    public SigningSessionListResponse list(SigningSessionListParams params) {
        if (params == null) {
            return http.requestWithQuery("GET", "/v1/signing-sessions",
                    SigningSessionListResponse.class, Collections.emptyMap());
        }
        return http.requestWithQuery("GET", "/v1/signing-sessions",
                SigningSessionListResponse.class, params.toQueryMap());
    }

    /**
     * Lists signing sessions with a per-request timeout.
     *
     * @param params  the list parameters, or null for defaults
     * @param timeout the request timeout
     * @return the signing session list response with pagination
     */
    public SigningSessionListResponse list(SigningSessionListParams params, Duration timeout) {
        if (params == null) {
            return http.requestWithQuery("GET", "/v1/signing-sessions",
                    SigningSessionListResponse.class, Collections.emptyMap(), timeout);
        }
        return http.requestWithQuery("GET", "/v1/signing-sessions",
                SigningSessionListResponse.class, params.toQueryMap(), timeout);
    }
}
