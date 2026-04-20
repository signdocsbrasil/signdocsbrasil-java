package com.signdocsbrasil.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.signdocsbrasil.api.errors.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * HTTP client wrapper for the SignDocsBrasil API.
 * Handles authentication, JSON serialization, retry logic, and error parsing.
 * Uses {@link java.net.http.HttpClient} (Java 11+).
 */
public final class HttpClient {

    private static final String SDK_VERSION = "1.3.0";
    private static final String USER_AGENT = "signdocs-brasil-java/" + SDK_VERSION;

    private final java.net.http.HttpClient client;
    private final String baseUrl;
    private final Duration timeout;
    private final AuthHandler auth;
    private final RetryHandler retryHandler;
    private final Gson gson;
    private final java.util.logging.Logger logger;
    private final Consumer<ResponseMetadata> onResponse;

    HttpClient(Config config, AuthHandler auth) {
        if (config.getHttpClient() != null) {
            this.client = config.getHttpClient();
        } else {
            this.client = java.net.http.HttpClient.newBuilder()
                    .connectTimeout(config.getTimeout())
                    .build();
        }
        this.baseUrl = config.getBaseUrl();
        this.timeout = config.getTimeout();
        this.auth = auth;
        this.retryHandler = new RetryHandler(config.getMaxRetries());
        this.gson = new GsonBuilder().create();
        this.logger = config.getLogger();
        this.onResponse = config.getOnResponse();
    }

    /**
     * Executes an authenticated HTTP request with retry logic.
     *
     * @param method         HTTP method (GET, POST, PUT, DELETE)
     * @param path           API path (e.g., "/v1/transactions")
     * @param body           request body object, or null for no body
     * @param responseType   the type to deserialize the response into
     * @param query          optional query parameters
     * @param headers        optional additional headers
     * @param noAuth         if true, skip adding the Authorization header
     * @param requestTimeout optional per-request timeout override, or null for default
     * @param <T>            the response type
     * @return the deserialized response, or null for 204 No Content
     */
    public <T> T request(String method, String path, Object body, Type responseType,
                  Map<String, String> query, Map<String, String> headers, boolean noAuth,
                  Duration requestTimeout) {
        long startTime = System.currentTimeMillis();

        for (int attempt = 0; attempt <= retryHandler.getMaxRetries(); attempt++) {
            retryHandler.checkTimeout(startTime);

            HttpResponse<String> response = executeRequest(method, path, body, query, headers, noAuth, requestTimeout);
            int statusCode = response.statusCode();
            long durationMs = System.currentTimeMillis() - startTime;

            // Log the request/response
            if (logger != null) {
                String logMessage = method + " " + path + " -> " + statusCode + " (" + durationMs + "ms)";
                if (statusCode >= 400) {
                    logger.warning(logMessage);
                } else {
                    logger.info(logMessage);
                }
            }

            // Fire the response-metadata observer. Exceptions are swallowed
            // so that an observer bug cannot break SDK requests.
            notifyResponseObserver(response, method, path);

            // If retryable and not on last attempt, retry with backoff
            if (retryHandler.isRetryable(statusCode) && attempt < retryHandler.getMaxRetries()) {
                long delay = retryHandler.calculateDelay(attempt, response);
                retryHandler.sleep(delay);
                continue;
            }

            // Parse the response
            return parseResponse(response, responseType);
        }

        throw new TimeoutException("Max retries exceeded");
    }

    /**
     * Executes an authenticated HTTP request with retry logic (default timeout).
     */
    public <T> T request(String method, String path, Object body, Type responseType,
                  Map<String, String> query, Map<String, String> headers, boolean noAuth) {
        return request(method, path, body, responseType, query, headers, noAuth, null);
    }

    /**
     * Executes an authenticated request (convenience overload).
     */
    public <T> T request(String method, String path, Object body, Type responseType) {
        return request(method, path, body, responseType, null, null, false, null);
    }

    /**
     * Executes an authenticated request with per-request timeout (convenience overload).
     */
    public <T> T request(String method, String path, Object body, Type responseType, Duration requestTimeout) {
        return request(method, path, body, responseType, null, null, false, requestTimeout);
    }

    /**
     * Executes an unauthenticated request (convenience overload).
     */
    public <T> T requestNoAuth(String method, String path, Type responseType) {
        return request(method, path, null, responseType, null, null, true, null);
    }

    /**
     * Executes an unauthenticated request with per-request timeout (convenience overload).
     */
    public <T> T requestNoAuth(String method, String path, Type responseType, Duration requestTimeout) {
        return request(method, path, null, responseType, null, null, true, requestTimeout);
    }

    /**
     * Executes a request with query parameters (convenience overload).
     */
    public <T> T requestWithQuery(String method, String path, Type responseType, Map<String, String> query) {
        return request(method, path, null, responseType, query, null, false, null);
    }

    /**
     * Executes a request with query parameters and per-request timeout (convenience overload).
     */
    public <T> T requestWithQuery(String method, String path, Type responseType, Map<String, String> query,
                                   Duration requestTimeout) {
        return request(method, path, null, responseType, query, null, false, requestTimeout);
    }

    /**
     * Executes a POST request with an automatic idempotency key.
     */
    public <T> T requestWithIdempotency(String method, String path, Object body, Type responseType,
                                  String idempotencyKey) {
        String key = idempotencyKey != null ? idempotencyKey : UUID.randomUUID().toString();
        return request(method, path, body, responseType, null,
                Map.of("X-Idempotency-Key", key), false, null);
    }

    /**
     * Executes a POST request with an automatic idempotency key and per-request timeout.
     */
    public <T> T requestWithIdempotency(String method, String path, Object body, Type responseType,
                                  String idempotencyKey, Duration requestTimeout) {
        String key = idempotencyKey != null ? idempotencyKey : UUID.randomUUID().toString();
        return request(method, path, body, responseType, null,
                Map.of("X-Idempotency-Key", key), false, requestTimeout);
    }

    Gson getGson() {
        return gson;
    }

    private void notifyResponseObserver(HttpResponse<String> response, String method, String path) {
        if (onResponse == null) {
            return;
        }
        try {
            ResponseMetadata metadata = ResponseMetadata.fromResponse(response, method, path);
            onResponse.accept(metadata);
        } catch (RuntimeException e) {
            if (logger != null) {
                logger.log(Level.WARNING, "onResponse callback threw: " + e.getMessage(), e);
            } else {
                System.getLogger(HttpClient.class.getName())
                        .log(System.Logger.Level.WARNING, "onResponse callback threw: " + e.getMessage(), e);
            }
        }
    }

    private HttpResponse<String> executeRequest(String method, String path, Object body,
                                                 Map<String, String> query,
                                                 Map<String, String> extraHeaders,
                                                 boolean noAuth,
                                                 Duration requestTimeout) {
        Duration effectiveTimeout = requestTimeout != null ? requestTimeout : timeout;
        try {
            String url = buildUrl(path, query);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(effectiveTimeout)
                    .header("User-Agent", USER_AGENT);

            // Add authentication
            if (!noAuth) {
                String token = auth.getAccessToken();
                requestBuilder.header("Authorization", "Bearer " + token);
            }

            // Add extra headers
            if (extraHeaders != null) {
                for (Map.Entry<String, String> entry : extraHeaders.entrySet()) {
                    requestBuilder.header(entry.getKey(), entry.getValue());
                }
            }

            // Set method and body
            if (body != null) {
                String jsonBody = gson.toJson(body);
                requestBuilder.header("Content-Type", "application/json");
                requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(jsonBody));
            } else if ("POST".equals(method) || "PUT".equals(method)) {
                // POST/PUT with empty body still needs Content-Type
                requestBuilder.header("Content-Type", "application/json");
                requestBuilder.method(method, HttpRequest.BodyPublishers.ofString("{}"));
            } else if ("DELETE".equals(method)) {
                requestBuilder.method("DELETE", HttpRequest.BodyPublishers.noBody());
            } else {
                requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());
            }

            return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (java.net.http.HttpTimeoutException e) {
            throw new TimeoutException("Request to " + path + " timed out after " + effectiveTimeout.toMillis() + "ms", e);
        } catch (IOException e) {
            throw new ConnectionException("Failed to connect to " + baseUrl + path + ": " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConnectionException("Request interrupted: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T parseResponse(HttpResponse<String> response, Type responseType) {
        int statusCode = response.statusCode();

        // Handle 204 No Content
        if (statusCode == 204) {
            return null;
        }

        String responseBody = response.body();
        String contentType = response.headers().firstValue("Content-Type").orElse("");

        // Handle error responses
        if (statusCode >= 400) {
            throwApiError(statusCode, responseBody, contentType, response);
        }

        // Handle empty body
        if (responseBody == null || responseBody.isEmpty()) {
            return null;
        }

        // Deserialize response
        return gson.fromJson(responseBody, responseType);
    }

    private void throwApiError(int statusCode, String body, String contentType,
                                HttpResponse<String> response) {
        ProblemDetail problemDetail;

        if ((contentType.contains("application/json") || contentType.contains("application/problem+json"))
                && body != null && !body.isEmpty()) {
            try {
                JsonObject json = gson.fromJson(body, JsonObject.class);
                if (json.has("type")) {
                    problemDetail = gson.fromJson(body, ProblemDetail.class);
                } else {
                    problemDetail = ProblemDetail.fallback(statusCode, body);
                }
            } catch (Exception e) {
                problemDetail = ProblemDetail.fallback(statusCode, body);
            }
        } else {
            problemDetail = ProblemDetail.fallback(statusCode, body);
        }

        Integer retryAfterSeconds = null;
        if (statusCode == 429) {
            String retryAfterHeader = response.headers().firstValue("Retry-After").orElse(null);
            if (retryAfterHeader != null) {
                try {
                    retryAfterSeconds = Integer.parseInt(retryAfterHeader);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        switch (statusCode) {
            case 400:
                throw new BadRequestException(problemDetail);
            case 401:
                throw new UnauthorizedException(problemDetail);
            case 403:
                throw new ForbiddenException(problemDetail);
            case 404:
                throw new NotFoundException(problemDetail);
            case 409:
                throw new ConflictException(problemDetail);
            case 422:
                throw new UnprocessableEntityException(problemDetail);
            case 429:
                throw new RateLimitException(problemDetail, retryAfterSeconds);
            case 500:
                throw new InternalServerException(problemDetail);
            case 503:
                throw new ServiceUnavailableException(problemDetail);
            default:
                throw new ApiException(problemDetail);
        }
    }

    private String buildUrl(String path, Map<String, String> query) {
        StringBuilder url = new StringBuilder(baseUrl);
        url.append(path);

        if (query != null && !query.isEmpty()) {
            url.append('?');
            boolean first = true;
            for (Map.Entry<String, String> entry : query.entrySet()) {
                if (entry.getValue() != null) {
                    if (!first) {
                        url.append('&');
                    }
                    url.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                    url.append('=');
                    url.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
                    first = false;
                }
            }
        }

        return url.toString();
    }
}
