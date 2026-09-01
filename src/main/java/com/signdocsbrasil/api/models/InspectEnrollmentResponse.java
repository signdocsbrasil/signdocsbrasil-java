package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Verdict for one candidate reference photo, from a dry run.
 *
 * <p>{@code marginal} is the one to act on: it would enrol without complaint
 * and is exactly what becomes a rejected signature later.
 */
public class InspectEnrollmentResponse {

    @SerializedName("dryRun")
    private Boolean dryRun;

    @SerializedName("userExternalId")
    private String userExternalId;

    @SerializedName("status")
    private String status;

    @SerializedName("error")
    private String error;

    @SerializedName("faceConfidence")
    private Double faceConfidence;

    @SerializedName("quality")
    private BatchEnrollmentModels.Quality quality;

    @SerializedName("pose")
    private BatchEnrollmentModels.Pose pose;

    @SerializedName("faceCoverage")
    private Double faceCoverage;

    @SerializedName("warnings")
    private List<String> warnings;

    public InspectEnrollmentResponse() {
    }

    public Boolean getDryRun() { return dryRun; }
    public String getUserExternalId() { return userExternalId; }
    public String getStatus() { return status; }
    public String getError() { return error; }
    public Double getFaceConfidence() { return faceConfidence; }
    public BatchEnrollmentModels.Quality getQuality() { return quality; }
    public BatchEnrollmentModels.Pose getPose() { return pose; }
    public Double getFaceCoverage() { return faceCoverage; }
    public List<String> getWarnings() { return warnings; }

    @Override
    public String toString() {
        return "InspectEnrollmentResponse{status='" + status + "', warnings=" + warnings + "}";
    }
}
