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
    void userAgentReflectsProjectVersion() {
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
            // Read from the pom rather than pinned to a literal: pinning is what
            // let this constant sit at 1.3.0, then 1.8.0, while the artifact
            // shipped 1.9.0 — every request reporting a version nobody was
            // running. A release that forgets SDK_VERSION now fails here.
            assertEquals("signdocs-brasil-java/" + projectVersion(), ua);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail(e);
        }
    }

    /** The {@code <version>} declared for this project in pom.xml. */
    private static String projectVersion() {
        try {
            String pom = new String(
                    java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("pom.xml")),
                    java.nio.charset.StandardCharsets.UTF_8);
            // The project's own version is the first <version> element, declared
            // above <dependencies>; dependency versions come later.
            java.util.regex.Matcher m = java.util.regex.Pattern
                    .compile("<version>([^<]+)</version>")
                    .matcher(pom.substring(0, pom.indexOf("<dependencies>")));
            if (!m.find()) {
                throw new IllegalStateException("no <version> found in pom.xml");
            }
            return m.group(1);
        } catch (java.io.IOException e) {
            throw new IllegalStateException("could not read pom.xml", e);
        }
    }
}
