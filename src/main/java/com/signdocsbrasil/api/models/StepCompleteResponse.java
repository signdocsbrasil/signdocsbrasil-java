package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from completing a step.
 */
public class StepCompleteResponse {

    @SerializedName("stepId")
    private String stepId;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("attempts")
    private int attempts;

    @SerializedName("result")
    private StepResult result;

    public StepCompleteResponse() {
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

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public StepResult getResult() {
        return result;
    }

    public void setResult(StepResult result) {
        this.result = result;
    }
}
