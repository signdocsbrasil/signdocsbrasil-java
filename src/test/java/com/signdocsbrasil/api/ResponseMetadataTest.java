package com.signdocsbrasil.api;

import com.google.gson.reflect.TypeToken;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ResponseMetadataTest {

    private MockWebServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    private String baseUrl() {
        return server.url("").toString().replaceAll("/$", "");
    }

    private HttpClient clientWithObserver(AtomicReference<ResponseMetadata> sink) {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .onResponse(sink::set)
                .build();
        return new HttpClient(config, new AuthHandler(config));
    }

    // ---- Parsing via direct helper ----------------------------------

    @Test
    void parsesRfc8594ImfFixdate() {
        Instant parsed = ResponseMetadata.rfc8594Date("Wed, 11 Nov 2026 08:49:37 GMT");
        assertNotNull(parsed);
        assertEquals(Instant.parse("2026-11-11T08:49:37Z"), parsed);
    }

    @Test
    void parsesRfc8594UnixSecondsForm() {
        Instant parsed = ResponseMetadata.rfc8594Date("@1793865600");
        assertNotNull(parsed);
        assertEquals(Instant.ofEpochSecond(1793865600L), parsed);
    }

    @Test
    void unparseableDateReturnsNull() {
        assertNull(ResponseMetadata.rfc8594Date("not a date at all"));
        assertNull(ResponseMetadata.rfc8594Date("@not-a-number"));
        assertNull(ResponseMetadata.rfc8594Date(""));
        assertNull(ResponseMetadata.rfc8594Date(null));
    }

    // ---- End-to-end via HttpClient ----------------------------------

    @Test
    void rateLimitHeadersParsedAsInts() {
        AtomicReference<ResponseMetadata> captured = new AtomicReference<>();
        HttpClient client = clientWithObserver(captured);

        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json")
                .setHeader("RateLimit-Limit", "100")
                .setHeader("RateLimit-Remaining", "42")
                .setHeader("RateLimit-Reset", "60"));

        client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        ResponseMetadata m = captured.get();
        assertNotNull(m);
        assertEquals(Integer.valueOf(100), m.getRateLimitLimit());
        assertEquals(Integer.valueOf(42), m.getRateLimitRemaining());
        assertEquals(Integer.valueOf(60), m.getRateLimitReset());
        assertEquals(200, m.getStatusCode());
        assertEquals("GET", m.getMethod());
        assertEquals("/health", m.getPath());
    }

    @Test
    void deprecationAndSunsetParsed() {
        AtomicReference<ResponseMetadata> captured = new AtomicReference<>();
        HttpClient client = clientWithObserver(captured);

        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json")
                .setHeader("Deprecation", "Wed, 11 Nov 2026 08:49:37 GMT")
                .setHeader("Sunset", "@1793865600"));

        client.requestNoAuth("GET", "/v1/old",
                new TypeToken<Map<String, Object>>() {}.getType());

        ResponseMetadata m = captured.get();
        assertNotNull(m);
        assertTrue(m.isDeprecated());
        assertEquals(Instant.parse("2026-11-11T08:49:37Z"), m.getDeprecation());
        assertEquals(Instant.ofEpochSecond(1793865600L), m.getSunset());
    }

    @Test
    void requestIdFallbackSignDocsHeader() {
        AtomicReference<ResponseMetadata> captured = new AtomicReference<>();
        HttpClient client = clientWithObserver(captured);

        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json")
                .setHeader("X-SignDocs-Request-Id", "req_abc123"));

        client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        assertEquals("req_abc123", captured.get().getRequestId());
    }

    @Test
    void requestIdPrefersXRequestId() {
        AtomicReference<ResponseMetadata> captured = new AtomicReference<>();
        HttpClient client = clientWithObserver(captured);

        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json")
                .setHeader("X-Request-Id", "req_from_primary")
                .setHeader("X-SignDocs-Request-Id", "req_from_secondary"));

        client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        assertEquals("req_from_primary", captured.get().getRequestId());
    }

    @Test
    void missingHeadersLeaveNullFields() {
        AtomicReference<ResponseMetadata> captured = new AtomicReference<>();
        HttpClient client = clientWithObserver(captured);

        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json"));

        client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        ResponseMetadata m = captured.get();
        assertNotNull(m);
        assertNull(m.getRateLimitLimit());
        assertNull(m.getRateLimitRemaining());
        assertNull(m.getRateLimitReset());
        assertNull(m.getDeprecation());
        assertNull(m.getSunset());
        assertNull(m.getRequestId());
        assertFalse(m.isDeprecated());
    }

    @Test
    void unparseableRateLimitHeadersBecomeNull() {
        AtomicReference<ResponseMetadata> captured = new AtomicReference<>();
        HttpClient client = clientWithObserver(captured);

        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json")
                .setHeader("RateLimit-Limit", "abc")
                .setHeader("Deprecation", "not a date"));

        client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        ResponseMetadata m = captured.get();
        assertNull(m.getRateLimitLimit());
        assertNull(m.getDeprecation());
    }
}
