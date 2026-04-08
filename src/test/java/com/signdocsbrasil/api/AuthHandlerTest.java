package com.signdocsbrasil.api;

import com.signdocsbrasil.api.errors.AuthenticationException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AuthHandlerTest {

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

    private AuthHandler createAuth() {
        return createAuth("test-secret", null, null);
    }

    private AuthHandler createAuth(String clientSecret, String privateKey, String kid) {
        Config.Builder builder = Config.builder()
                .clientId("test-client")
                .baseUrl(server.url("").toString().replaceAll("/$", ""));

        if (clientSecret != null) {
            builder.clientSecret(clientSecret);
        }
        if (privateKey != null) {
            builder.privateKey(privateKey).kid(kid);
        }

        return new AuthHandler(builder.build());
    }

    @Test
    void clientSecretFlow() throws Exception {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok_123\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        AuthHandler auth = createAuth();
        String token = auth.getAccessToken();

        assertEquals("tok_123", token);

        RecordedRequest req = server.takeRequest();
        assertEquals("POST", req.getMethod());
        assertTrue(req.getHeader("Content-Type").contains("application/x-www-form-urlencoded"));
        String body = req.getBody().readUtf8();
        assertTrue(body.contains("grant_type=client_credentials"));
        assertTrue(body.contains("client_id=test-client"));
        assertTrue(body.contains("client_secret=test-secret"));
    }

    @Test
    void tokenCaching() throws Exception {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok_cached\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        AuthHandler auth = createAuth();
        String t1 = auth.getAccessToken();
        String t2 = auth.getAccessToken();

        assertEquals("tok_cached", t1);
        assertEquals("tok_cached", t2);
        assertEquals(1, server.getRequestCount());
    }

    @Test
    void refreshWithin30sBuffer() throws Exception {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok_1\",\"expires_in\":20}")
                .setHeader("Content-Type", "application/json"));
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok_2\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        AuthHandler auth = createAuth();
        String t1 = auth.getAccessToken();
        assertEquals("tok_1", t1);

        // 20s < 30s buffer, next call should refresh
        String t2 = auth.getAccessToken();
        assertEquals("tok_2", t2);
        assertEquals(2, server.getRequestCount());
    }

    @Test
    void errorOnNon200Response() {
        server.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody("unauthorized"));

        AuthHandler auth = createAuth();
        assertThrows(AuthenticationException.class, auth::getAccessToken);
    }

    @Test
    void formUrlEncodedContentType() throws Exception {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        AuthHandler auth = createAuth();
        auth.getAccessToken();

        RecordedRequest req = server.takeRequest();
        assertTrue(req.getHeader("Content-Type").contains("application/x-www-form-urlencoded"));
    }

    @Test
    void scopesSent() throws Exception {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        AuthHandler auth = createAuth();
        auth.getAccessToken();

        RecordedRequest req = server.takeRequest();
        String body = req.getBody().readUtf8();
        assertTrue(body.contains("scope="));
    }
}
