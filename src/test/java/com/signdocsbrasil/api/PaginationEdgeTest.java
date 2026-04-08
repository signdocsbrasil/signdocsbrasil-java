package com.signdocsbrasil.api;

import com.signdocsbrasil.api.models.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Phase 6: Pagination edge case tests for Java SDK.
 * Tests empty results, single page, multi-page, limit boundaries,
 * and nextToken propagation.
 */
class PaginationEdgeTest {

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

    private void enqueueToken() {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));
    }

    private SignDocsBrasilClient createClient() {
        String baseUrl = server.url("").toString().replaceAll("/$", "");
        return SignDocsBrasilClient.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl)
                .maxRetries(0)
                .build();
    }

    private String txJson(String id) {
        return String.format(
                "{\"tenantId\":\"ten_1\",\"transactionId\":\"%s\",\"status\":\"COMPLETED\",\"purpose\":\"DOCUMENT_SIGNATURE\"," +
                        "\"policy\":{\"profile\":\"CLICK_ONLY\"},\"signer\":{\"name\":\"Test\"},\"steps\":[],\"createdAt\":\"2024-01-01T00:00:00Z\"}",
                id);
    }

    // ── Standard list() ───────────────────────────────────────

    @Test
    void emptyFirstPage() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[],\"count\":0}")
                .setHeader("Content-Type", "application/json"));

        TransactionListResponse resp = client.transactions().list(null);

        assertNotNull(resp);
        assertTrue(resp.getTransactions().isEmpty());
        assertEquals(0, resp.getCount());
        assertNull(resp.getNextToken());
    }

    @Test
    void singlePageNoNextToken() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_1") + "," + txJson("tx_2") + "],\"count\":2}")
                .setHeader("Content-Type", "application/json"));

        TransactionListResponse resp = client.transactions().list(null);

        assertEquals(2, resp.getTransactions().size());
        assertEquals(2, resp.getCount());
        assertNull(resp.getNextToken());
        assertEquals("tx_1", resp.getTransactions().get(0).getTransactionId());
        assertEquals("tx_2", resp.getTransactions().get(1).getTransactionId());
    }

    @Test
    void nextTokenForwardedInQuery() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_3") + "],\"count\":1,\"nextToken\":\"page3\"}")
                .setHeader("Content-Type", "application/json"));

        TransactionListParams params = new TransactionListParams();
        params.setNextToken("page2");
        TransactionListResponse resp = client.transactions().list(params);

        assertEquals("page3", resp.getNextToken());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertTrue(apiReq.getPath().contains("nextToken=page2"));
    }

    @Test
    void limitOneReturnsSingleItem() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_1") + "],\"count\":1,\"nextToken\":\"next\"}")
                .setHeader("Content-Type", "application/json"));

        TransactionListParams params = new TransactionListParams();
        params.setLimit(1);
        TransactionListResponse resp = client.transactions().list(params);

        assertEquals(1, resp.getTransactions().size());
        assertEquals("next", resp.getNextToken());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertTrue(apiReq.getPath().contains("limit=1"));
    }

    @Test
    void maxLimit100() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();

        String items = IntStream.range(0, 100)
                .mapToObj(i -> txJson("tx_" + i))
                .collect(Collectors.joining(","));
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + items + "],\"count\":100,\"nextToken\":\"more\"}")
                .setHeader("Content-Type", "application/json"));

        TransactionListParams params = new TransactionListParams();
        params.setLimit(100);
        TransactionListResponse resp = client.transactions().list(params);

        assertEquals(100, resp.getTransactions().size());
        assertEquals("more", resp.getNextToken());
    }

    @Test
    void nullNextTokenMeansEnd() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_last") + "],\"count\":1,\"nextToken\":null}")
                .setHeader("Content-Type", "application/json"));

        TransactionListResponse resp = client.transactions().list(null);

        assertNull(resp.getNextToken());
    }

    // ── Manual multi-page pagination ────────────────────────────

    @Test
    void manualPaginationAcrossTwoPages() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        // Page 1
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_1") + "," + txJson("tx_2") + "],\"count\":2,\"nextToken\":\"page2\"}")
                .setHeader("Content-Type", "application/json"));
        // Page 2
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_3") + "],\"count\":1}")
                .setHeader("Content-Type", "application/json"));

        // Fetch page 1
        TransactionListResponse page1 = client.transactions().list(null);
        assertEquals(2, page1.getTransactions().size());
        assertEquals("page2", page1.getNextToken());

        // Fetch page 2 using nextToken
        TransactionListParams params = new TransactionListParams();
        params.setNextToken(page1.getNextToken());
        TransactionListResponse page2 = client.transactions().list(params);
        assertEquals(1, page2.getTransactions().size());
        assertNull(page2.getNextToken());

        // Total: token + 2 API calls
        assertEquals(3, server.getRequestCount());
    }

    @Test
    void manualPaginationThreePages() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_1") + "],\"count\":1,\"nextToken\":\"p2\"}")
                .setHeader("Content-Type", "application/json"));
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_2") + "],\"count\":1,\"nextToken\":\"p3\"}")
                .setHeader("Content-Type", "application/json"));
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_3") + "],\"count\":1}")
                .setHeader("Content-Type", "application/json"));

        int totalItems = 0;
        String nextToken = null;
        do {
            TransactionListParams params = new TransactionListParams();
            if (nextToken != null) params.setNextToken(nextToken);
            TransactionListResponse page = client.transactions().list(params);
            totalItems += page.getTransactions().size();
            nextToken = page.getNextToken();
        } while (nextToken != null);

        assertEquals(3, totalItems);
        // 1 token + 3 API calls
        assertEquals(4, server.getRequestCount());
    }

    @Test
    void manualPaginationEmptySecondPageTerminates() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_1") + "],\"count\":1,\"nextToken\":\"page2\"}")
                .setHeader("Content-Type", "application/json"));
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[],\"count\":0}")
                .setHeader("Content-Type", "application/json"));

        TransactionListResponse page1 = client.transactions().list(null);
        assertEquals(1, page1.getTransactions().size());
        assertNotNull(page1.getNextToken());

        TransactionListParams params = new TransactionListParams();
        params.setNextToken(page1.getNextToken());
        TransactionListResponse page2 = client.transactions().list(params);
        assertTrue(page2.getTransactions().isEmpty());
        assertNull(page2.getNextToken());
    }

    // ── Auto-paginate ───────────────────────────────────────────

    @Test
    void autoPaginateTwoPages() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        // Page 1: two items with nextToken
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_1") + "," + txJson("tx_2") + "],\"count\":2,\"nextToken\":\"page2\"}")
                .setHeader("Content-Type", "application/json"));
        // Page 2: one item, no nextToken
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_3") + "],\"count\":1}")
                .setHeader("Content-Type", "application/json"));

        List<Transaction> all = new ArrayList<>();
        for (Transaction tx : client.transactions().listAutoPaginate()) {
            all.add(tx);
        }

        assertEquals(3, all.size());
        assertEquals("tx_1", all.get(0).getTransactionId());
        assertEquals("tx_2", all.get(1).getTransactionId());
        assertEquals("tx_3", all.get(2).getTransactionId());
        // 1 token + 2 API calls
        assertEquals(3, server.getRequestCount());
    }

    @Test
    void autoPaginateEmptyResult() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[],\"count\":0}")
                .setHeader("Content-Type", "application/json"));

        List<Transaction> all = new ArrayList<>();
        for (Transaction tx : client.transactions().listAutoPaginate()) {
            all.add(tx);
        }

        assertTrue(all.isEmpty());
    }

    @Test
    void autoPaginateStopsOnNullNextToken() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        // Single page, no nextToken
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[" + txJson("tx_only") + "],\"count\":1}")
                .setHeader("Content-Type", "application/json"));

        List<Transaction> all = new ArrayList<>();
        TransactionListParams params = new TransactionListParams();
        params.setStatus("COMPLETED");
        for (Transaction tx : client.transactions().listAutoPaginate(params)) {
            all.add(tx);
        }

        assertEquals(1, all.size());
        assertEquals("tx_only", all.get(0).getTransactionId());
        // 1 token + 1 API call
        assertEquals(2, server.getRequestCount());
    }
}
