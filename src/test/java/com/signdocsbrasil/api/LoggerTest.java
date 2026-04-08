package com.signdocsbrasil.api;

import com.google.gson.reflect.TypeToken;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the logging functionality in {@link HttpClient}.
 * Uses a custom {@link Handler} to capture log records for assertions.
 */
class LoggerTest {

    private MockWebServer server;
    private Logger logger;
    private CapturingHandler handler;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();

        logger = Logger.getLogger("com.signdocsbrasil.api.test." + System.nanoTime());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        handler = new CapturingHandler();
        logger.addHandler(handler);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
        logger.removeHandler(handler);
    }

    private String baseUrl() {
        return server.url("").toString().replaceAll("/$", "");
    }

    private HttpClient createHttpClient(Logger log) {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .logger(log)
                .build();
        AuthHandler auth = new AuthHandler(config);
        return new HttpClient(config, auth);
    }

    @Test
    void logsSuccessfulRequestAtInfoLevel() {
        // No token needed for noAuth
        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"healthy\"}")
                .setHeader("Content-Type", "application/json"));

        HttpClient client = createHttpClient(logger);
        client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        assertEquals(1, handler.records.size());
        LogRecord record = handler.records.get(0);
        assertEquals(Level.INFO, record.getLevel());
        assertTrue(record.getMessage().contains("GET"));
        assertTrue(record.getMessage().contains("/health"));
        assertTrue(record.getMessage().contains("200"));
        assertTrue(record.getMessage().contains("ms"));
    }

    @Test
    void logsClientErrorAtWarningLevel() {
        server.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("{\"type\":\"about:blank\",\"title\":\"Not Found\",\"status\":404}")
                .setHeader("Content-Type", "application/json"));

        HttpClient client = createHttpClient(logger);
        try {
            client.requestNoAuth("GET", "/v1/missing",
                    new TypeToken<Map<String, Object>>() {}.getType());
            fail("Should have thrown");
        } catch (Exception e) {
            // Expected
        }

        assertEquals(1, handler.records.size());
        LogRecord record = handler.records.get(0);
        assertEquals(Level.WARNING, record.getLevel());
        assertTrue(record.getMessage().contains("404"));
        assertTrue(record.getMessage().contains("/v1/missing"));
    }

    @Test
    void logsServerErrorAtWarningLevel() {
        server.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{\"type\":\"about:blank\",\"title\":\"Internal Server Error\",\"status\":500}")
                .setHeader("Content-Type", "application/json"));

        HttpClient client = createHttpClient(logger);
        try {
            client.requestNoAuth("POST", "/v1/fail",
                    new TypeToken<Map<String, Object>>() {}.getType());
            fail("Should have thrown");
        } catch (Exception e) {
            // Expected
        }

        assertEquals(1, handler.records.size());
        LogRecord record = handler.records.get(0);
        assertEquals(Level.WARNING, record.getLevel());
        assertTrue(record.getMessage().contains("500"));
    }

    @Test
    void logDoesNotContainAuthorizationHeader() throws Exception {
        // Enqueue token response + successful API response
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"super-secret-token-value\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));
        server.enqueue(new MockResponse()
                .setBody("{\"id\":\"tx_1\"}")
                .setHeader("Content-Type", "application/json"));

        HttpClient client = createHttpClient(logger);
        client.request("GET", "/v1/transactions/tx_1", null,
                new TypeToken<Map<String, Object>>() {}.getType());

        // Check that no log record contains the token or Authorization
        for (LogRecord record : handler.records) {
            String msg = record.getMessage();
            assertFalse(msg.contains("Authorization"), "Log should not contain Authorization header");
            assertFalse(msg.contains("super-secret-token-value"), "Log should not contain token value");
            assertFalse(msg.contains("Bearer"), "Log should not contain Bearer prefix");
        }
    }

    @Test
    void nullLoggerDoesNotCauseErrors() {
        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"healthy\"}")
                .setHeader("Content-Type", "application/json"));

        // Create client with null logger (default behavior)
        HttpClient client = createHttpClient(null);
        var result = client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        assertNotNull(result);
        // No exceptions should have occurred
    }

    @Test
    void logContainsMethodPathStatusAndDuration() {
        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json"));

        HttpClient client = createHttpClient(logger);
        client.requestNoAuth("GET", "/v1/test-path",
                new TypeToken<Map<String, Object>>() {}.getType());

        assertEquals(1, handler.records.size());
        String msg = handler.records.get(0).getMessage();

        // Verify all expected components are present
        assertTrue(msg.contains("GET"), "Log should contain HTTP method");
        assertTrue(msg.contains("/v1/test-path"), "Log should contain request path");
        assertTrue(msg.contains("200"), "Log should contain status code");
        assertTrue(msg.matches(".*\\d+ms.*"), "Log should contain duration in milliseconds");
    }

    @Test
    void loggerViaSignDocsBrasilClientBuilder() {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .logger(logger)
                .build();

        assertSame(logger, config.getLogger());
    }

    /**
     * Custom log handler that captures all log records for testing.
     */
    private static class CapturingHandler extends Handler {
        final List<LogRecord> records = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }
}
