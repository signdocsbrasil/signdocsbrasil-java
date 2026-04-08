package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response containing a list of steps for a transaction.
 */
public class StepListResponse {

    @SerializedName("steps")
    private List<StepDetail> steps;

    public StepListResponse() {
    }

    public List<StepDetail> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDetail> steps) {
        this.steps = steps;
    }

    /**
     * Detailed information about a verification step.
     */
    public static class StepDetail {

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

        @SerializedName("error")
        private String error;

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

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
