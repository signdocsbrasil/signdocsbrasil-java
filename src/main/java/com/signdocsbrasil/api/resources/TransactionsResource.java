package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.CancelTransactionResponse;
import com.signdocsbrasil.api.models.CreateTransactionRequest;
import com.signdocsbrasil.api.models.FinalizeResponse;
import com.signdocsbrasil.api.models.Transaction;
import com.signdocsbrasil.api.models.TransactionListParams;
import com.signdocsbrasil.api.models.TransactionListResponse;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Resource for transaction CRUD operations.
 */
public final class TransactionsResource {

    private final HttpClient http;

    public TransactionsResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Creates a new signing transaction with an automatically generated idempotency key.
     *
     * @param request the transaction creation request
     * @return the created transaction
     */
    public Transaction create(CreateTransactionRequest request) {
        return create(request, (String) null);
    }

    /**
     * Creates a new signing transaction with a specified idempotency key.
     *
     * @param request        the transaction creation request
     * @param idempotencyKey the idempotency key, or null to auto-generate
     * @return the created transaction
     */
    public Transaction create(CreateTransactionRequest request, String idempotencyKey) {
        return http.requestWithIdempotency("POST", "/v1/transactions", request,
                Transaction.class, idempotencyKey);
    }

    /**
     * Creates a new signing transaction with a per-request timeout.
     *
     * @param request the transaction creation request
     * @param timeout the request timeout
     * @return the created transaction
     */
    public Transaction create(CreateTransactionRequest request, Duration timeout) {
        return http.requestWithIdempotency("POST", "/v1/transactions", request,
                Transaction.class, null, timeout);
    }

    /**
     * Creates a new signing transaction with a specified idempotency key and per-request timeout.
     *
     * @param request        the transaction creation request
     * @param idempotencyKey the idempotency key, or null to auto-generate
     * @param timeout        the request timeout
     * @return the created transaction
     */
    public Transaction create(CreateTransactionRequest request, String idempotencyKey, Duration timeout) {
        return http.requestWithIdempotency("POST", "/v1/transactions", request,
                Transaction.class, idempotencyKey, timeout);
    }

    /**
     * Lists transactions with default parameters (no filters).
     *
     * @return the transaction list response with pagination
     */
    public TransactionListResponse list() {
        return list(null);
    }

    /**
     * Lists transactions with the given filter and pagination parameters.
     *
     * @param params the list parameters (status, userExternalId, limit, nextToken, etc.)
     * @return the transaction list response with pagination
     */
    public TransactionListResponse list(TransactionListParams params) {
        if (params == null) {
            return http.requestWithQuery("GET", "/v1/transactions",
                    TransactionListResponse.class, Collections.emptyMap());
        }
        return http.requestWithQuery("GET", "/v1/transactions",
                TransactionListResponse.class, params.toQueryMap());
    }

    /**
     * Lists transactions with a per-request timeout.
     *
     * @param params  the list parameters, or null for defaults
     * @param timeout the request timeout
     * @return the transaction list response with pagination
     */
    public TransactionListResponse list(TransactionListParams params, Duration timeout) {
        if (params == null) {
            return http.requestWithQuery("GET", "/v1/transactions",
                    TransactionListResponse.class, Collections.emptyMap(), timeout);
        }
        return http.requestWithQuery("GET", "/v1/transactions",
                TransactionListResponse.class, params.toQueryMap(), timeout);
    }

    /**
     * Retrieves a single transaction by ID.
     *
     * @param transactionId the transaction ID
     * @return the transaction
     */
    public Transaction get(String transactionId) {
        return http.request("GET", "/v1/transactions/" + transactionId,
                null, Transaction.class);
    }

    /**
     * Retrieves a single transaction by ID with a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param timeout       the request timeout
     * @return the transaction
     */
    public Transaction get(String transactionId, Duration timeout) {
        return http.request("GET", "/v1/transactions/" + transactionId,
                null, Transaction.class, timeout);
    }

    /**
     * Cancels a transaction. Returns the cancel response (HTTP 200 with JSON body).
     *
     * @param transactionId the transaction ID to cancel
     * @return the cancel transaction response
     */
    public CancelTransactionResponse cancel(String transactionId) {
        return http.request("DELETE", "/v1/transactions/" + transactionId,
                null, CancelTransactionResponse.class);
    }

    /**
     * Cancels a transaction with a per-request timeout.
     *
     * @param transactionId the transaction ID to cancel
     * @param timeout       the request timeout
     * @return the cancel transaction response
     */
    public CancelTransactionResponse cancel(String transactionId, Duration timeout) {
        return http.request("DELETE", "/v1/transactions/" + transactionId,
                null, CancelTransactionResponse.class, timeout);
    }

    /**
     * Finalizes a transaction, completing the signing process.
     *
     * @param transactionId the transaction ID to finalize
     * @return the finalize response
     */
    public FinalizeResponse finalize(String transactionId) {
        return http.request("POST", "/v1/transactions/" + transactionId + "/finalize",
                null, FinalizeResponse.class);
    }

    /**
     * Finalizes a transaction with a per-request timeout.
     *
     * @param transactionId the transaction ID to finalize
     * @param timeout       the request timeout
     * @return the finalize response
     */
    public FinalizeResponse finalize(String transactionId, Duration timeout) {
        return http.request("POST", "/v1/transactions/" + transactionId + "/finalize",
                null, FinalizeResponse.class, timeout);
    }

    /**
     * Returns an {@link Iterable} that automatically paginates through all transactions
     * matching the given parameters. The caller's {@code nextToken} is ignored; pagination
     * is managed internally.
     *
     * <p>Usage:
     * <pre>{@code
     * TransactionListParams params = new TransactionListParams();
     * params.setStatus("COMPLETED");
     * for (Transaction tx : client.transactions().listAutoPaginate(params)) {
     *     System.out.println(tx.getTransactionId());
     * }
     * }</pre>
     *
     * @param params optional filter parameters (nextToken is managed automatically)
     * @return an iterable over all matching transactions across all pages
     */
    public Iterable<Transaction> listAutoPaginate(TransactionListParams params) {
        return () -> new Iterator<Transaction>() {
            private final TransactionListParams pageParams = copyParams(params);
            private List<Transaction> currentPage = null;
            private int index = 0;
            private boolean done = false;

            @Override
            public boolean hasNext() {
                if (done) {
                    return false;
                }
                if (currentPage != null && index < currentPage.size()) {
                    return true;
                }
                // Need to fetch next page
                if (currentPage != null && pageParams.getNextToken() == null) {
                    done = true;
                    return false;
                }
                TransactionListResponse response = list(pageParams);
                currentPage = response.getTransactions() != null
                        ? response.getTransactions() : new ArrayList<>();
                index = 0;
                pageParams.setNextToken(response.getNextToken());
                if (currentPage.isEmpty()) {
                    done = true;
                    return false;
                }
                return true;
            }

            @Override
            public Transaction next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return currentPage.get(index++);
            }
        };
    }

    /**
     * Returns an {@link Iterable} that automatically paginates through all transactions.
     *
     * @return an iterable over all transactions across all pages
     */
    public Iterable<Transaction> listAutoPaginate() {
        return listAutoPaginate(null);
    }

    private static TransactionListParams copyParams(TransactionListParams source) {
        TransactionListParams copy = new TransactionListParams();
        if (source != null) {
            copy.setStatus(source.getStatus());
            copy.setUserExternalId(source.getUserExternalId());
            copy.setDocumentGroupId(source.getDocumentGroupId());
            copy.setLimit(source.getLimit());
            copy.setStartDate(source.getStartDate());
            copy.setEndDate(source.getEndDate());
        }
        // nextToken intentionally not copied — managed internally
        return copy;
    }
}
