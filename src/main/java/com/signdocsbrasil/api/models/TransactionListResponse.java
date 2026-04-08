package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response from listing transactions, with pagination support.
 */
public class TransactionListResponse {

    @SerializedName("transactions")
    private List<Transaction> transactions;

    @SerializedName("nextToken")
    private String nextToken;

    @SerializedName("count")
    private int count;

    public TransactionListResponse() {
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TransactionListResponse{count=" + count + ", hasNextToken=" + (nextToken != null) + "}";
    }
}
