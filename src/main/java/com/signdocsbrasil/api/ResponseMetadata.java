package com.signdocsbrasil.api;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Captures response-level metadata that's typically consumed for
 * observability and lifecycle signaling: rate-limit counters
 * (IETF RateLimit headers), RFC 8594 {@code Deprecation}/{@code Sunset}
 * signaling, and the upstream request ID.
 *
 * <p>Exposed via the {@code onResponse} callback in {@link Config}. The
 * SDK does not otherwise surface these headers to resource methods, so
 * the callback is the single place to plug in observability.
 */
public final class ResponseMetadata {

    private static final List<String> REQUEST_ID_HEADERS =
            Arrays.asList("X-Request-Id", "X-SignDocs-Request-Id");

    private final Integer rateLimitLimit;
    private final Integer rateLimitRemaining;
    private final Integer rateLimitReset;
    private final Instant deprecation;
    private final Instant sunset;
    private final String requestId;
    private final int statusCode;
    private final String method;
    private final String path;

    public ResponseMetadata(
            Integer rateLimitLimit,
            Integer rateLimitRemaining,
            Integer rateLimitReset,
            Instant deprecation,
            Instant sunset,
            String requestId,
            int statusCode,
            String method,
            String path) {
        this.rateLimitLimit = rateLimitLimit;
        this.rateLimitRemaining = rateLimitRemaining;
        this.rateLimitReset = rateLimitReset;
        this.deprecation = deprecation;
        this.sunset = sunset;
        this.requestId = requestId;
        this.statusCode = statusCode;
        this.method = method == null ? null : method.toUpperCase();
        this.path = path;
    }

    /**
     * Build a {@link ResponseMetadata} from an {@link HttpResponse}.
     */
    public static ResponseMetadata fromResponse(HttpResponse<?> response, String method, String path) {
        HttpHeaders headers = response.headers();
        return new ResponseMetadata(
                intHeader(headers, "RateLimit-Limit"),
                intHeader(headers, "RateLimit-Remaining"),
                intHeader(headers, "RateLimit-Reset"),
                rfc8594Date(firstHeader(headers, "Deprecation")),
                rfc8594Date(firstHeader(headers, "Sunset")),
                firstHeaderFrom(headers, REQUEST_ID_HEADERS),
                response.statusCode(),
                method,
                path);
    }

    public Integer getRateLimitLimit() {
        return rateLimitLimit;
    }

    public Integer getRateLimitRemaining() {
        return rateLimitRemaining;
    }

    public Integer getRateLimitReset() {
        return rateLimitReset;
    }

    public Instant getDeprecation() {
        return deprecation;
    }

    public Instant getSunset() {
        return sunset;
    }

    public String getRequestId() {
        return requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    /**
     * True if the endpoint is marked deprecated (a {@code Deprecation}
     * header was present and parseable).
     */
    public boolean isDeprecated() {
        return deprecation != null;
    }

    private static Integer intHeader(HttpHeaders headers, String name) {
        String value = firstHeader(headers, name);
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (!value.matches("-?\\d+")) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String firstHeader(HttpHeaders headers, String name) {
        return headers.firstValue(name).orElse(null);
    }

    private static String firstHeaderFrom(HttpHeaders headers, List<String> names) {
        for (String name : names) {
            Optional<String> v = headers.firstValue(name);
            if (v.isPresent() && !v.get().isEmpty()) {
                return v.get();
            }
        }
        return null;
    }

    /**
     * Parse an RFC 8594 {@code Deprecation}/{@code Sunset} header. Accepts
     * either an IMF-fixdate (HTTP-date) or an {@code @<unix-seconds>}
     * form. Returns {@code null} for any unparseable input.
     */
    static Instant rfc8594Date(String raw) {
        if (raw == null) {
            return null;
        }
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        if (trimmed.charAt(0) == '@') {
            String digits = trimmed.substring(1);
            if (!digits.matches("-?\\d+")) {
                return null;
            }
            try {
                return Instant.ofEpochSecond(Long.parseLong(digits));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        try {
            return DateTimeFormatter.RFC_1123_DATE_TIME.parse(trimmed, Instant::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
