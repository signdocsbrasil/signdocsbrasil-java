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

    @Override
    public String toString() {
        return "Policy{profile='" + profile + "', customSteps=" + customSteps + "}";
    }
}
