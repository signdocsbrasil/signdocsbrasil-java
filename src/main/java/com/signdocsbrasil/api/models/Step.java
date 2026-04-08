package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a verification step within a transaction.
 */
public class Step {

    @SerializedName("tenantId")
    private String tenantId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("stepId")
    private String stepId;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("order")
    private int order;

    @SerializedName("attempts")
    private int attempts;

    @SerializedName("maxAttempts")
    private int maxAttempts;

    @SerializedName("captureMode")
    private String captureMode;

    @SerializedName("startedAt")
    private String startedAt;

    @SerializedName("completedAt")
    private String completedAt;

    @SerializedName("result")
    private StepResult result;

    @SerializedName("error")
    private String error;

    public Step() {
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public String getCaptureMode() {
        return captureMode;
    }

    public void setCaptureMode(String captureMode) {
        this.captureMode = captureMode;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public StepResult getResult() {
        return result;
    }

    public void setResult(StepResult result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Step{stepId='" + stepId + "', type='" + type + "', status='" + status + "'}";
    }
}
