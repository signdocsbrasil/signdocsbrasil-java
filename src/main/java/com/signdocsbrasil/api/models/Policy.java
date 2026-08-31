package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Signing policy that defines the verification steps required.
 */
public class Policy {

    @SerializedName("profile")
    private String profile;

    @SerializedName("customSteps")
    private List<String> customSteps;

    /**
     * Minimum facial-match similarity this transaction requires, for the
     * BIOMETRIC_MATCH and DOCUMENT_PHOTO_MATCH steps.
     *
     * <p>Tightens only: the value must be at or above the tenant's configured
     * threshold, and anything lower is rejected with 400 naming the current
     * minimum rather than being silently ignored — loosening identity checking
     * is the tenant's decision, not the caller's. Accepts a percentage (95) or
     * a fraction (0.95). Boxed so an unset bar is omitted rather than sent as
     * 0, which the API would reject.
     */
    @SerializedName("minSimilarity")
    private Double minSimilarity;

    /**
     * Minimum liveness confidence this transaction requires
     * (BIOMETRIC_LIVENESS). Same rule as {@link #minSimilarity}.
     */
    @SerializedName("minLivenessConfidence")
    private Double minLivenessConfidence;

    public Policy() {
    }

    public Policy(String profile) {
        this.profile = profile;
    }

    public Policy(String profile, List<String> customSteps) {
        this.profile = profile;
        this.customSteps = customSteps;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public List<String> getCustomSteps() {
        return customSteps;
    }

    public void setCustomSteps(List<String> customSteps) {
        this.customSteps = customSteps;
    }

    public Double getMinSimilarity() {
        return minSimilarity;
    }

    public void setMinSimilarity(Double minSimilarity) {
        this.minSimilarity = minSimilarity;
    }

    public Double getMinLivenessConfidence() {
        return minLivenessConfidence;
    }

    public void setMinLivenessConfidence(Double minLivenessConfidence) {
        this.minLivenessConfidence = minLivenessConfidence;
    }

    @Override
    public String toString() {
        return "Policy{profile='" + profile + "', customSteps=" + customSteps
                + ", minSimilarity=" + minSimilarity
                + ", minLivenessConfidence=" + minLivenessConfidence + "}";
    }
}
