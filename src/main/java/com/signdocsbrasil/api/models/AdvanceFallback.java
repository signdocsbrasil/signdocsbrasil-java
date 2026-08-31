package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Present when the policy diverted to an alternative step instead of failing
 * outright — for example biometrics falling back to document-photo comparison.
 */
public class AdvanceFallback {

    @SerializedName("triggered")
    private boolean triggered;

    @SerializedName("reason")
    private String reason;

    @SerializedName("nextStepType")
    private String nextStepType;

    public AdvanceFallback() {
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNextStepType() {
        return nextStepType;
    }

    public void setNextStepType(String nextStepType) {
        this.nextStepType = nextStepType;
    }

    @Override
    public String toString() {
        return "AdvanceFallback{triggered=" + triggered + ", reason='" + reason
                + "', nextStepType='" + nextStepType + "'}";
    }
}
