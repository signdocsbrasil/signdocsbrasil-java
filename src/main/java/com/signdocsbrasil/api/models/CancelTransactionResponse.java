package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from cancelling a transaction.
 */
public class CancelTransactionResponse {

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("status")
    private String status;

    @SerializedName("cancelledAt")
    private String cancelledAt;

    public CancelTransactionResponse() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(String cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
