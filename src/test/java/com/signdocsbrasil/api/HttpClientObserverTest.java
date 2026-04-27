package com.signdocsbrasil.api;

import com.google.gson.reflect.TypeToken;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientObserverTest {

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

    @Test
    void callbackFiresForSuccessfulRequest() {
        AtomicInteger count = new AtomicInteger();

        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .onResponse(m -> count.incrementAndGet())
                .build();
        HttpClient client = new HttpClient(config, new AuthHandler(config));

        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json"));

        client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        assertEquals(1, count.get());
    }

    @Test
    void callbackExceptionDoesNotBreakRequest() {
        AtomicInteger invocations = new AtomicInteger();

        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .onResponse(m -> {
                    invocations.incrementAndGet();
                    throw new RuntimeException("boom");
                })
                .build();
        HttpClient client = new HttpClient(config, new AuthHandler(config));

        server.enqueue(new MockResponse()
                .setBody("{\"id\":\"tx_1\"}")
                .setHeader("Content-Type", "application/json"));

        // Should complete normally despite the observer exception.
        Object result = client.requestNoAuth("GET", "/v1/transactions/tx_1",
                new TypeToken<Map<String, Object>>() {}.getType());

        assertNotNull(result);
        assertEquals(1, invocations.get());
    }

    @Test
    void callbackFiresForErrorResponse() {
        AtomicInteger count = new AtomicInteger();

        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .onResponse(m -> count.incrementAndGet())
                .build();
        HttpClient client = new HttpClient(config, new AuthHandler(config));

        server.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("{\"type\":\"about:blank\",\"title\":\"Not Found\",\"status\":404}")
                .setHeader("Content-Type", "application/json"));

        try {
            client.requestNoAuth("GET", "/v1/missing",
                    new TypeToken<Map<String, Object>>() {}.getType());
            fail("should throw");
        } catch (Exception expected) {
            // expected
        }

        assertEquals(1, count.get());
    }

    @Test
    void userAgentReflects130() {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .build();
        HttpClient client = new HttpClient(config, new AuthHandler(config));

        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json"));

        client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        try {
            String ua = server.takeRequest().getHeader("User-Agent");
            assertEquals("signdocs-brasil-java/1.5.0", ua);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail(e);
        }
    }
}
