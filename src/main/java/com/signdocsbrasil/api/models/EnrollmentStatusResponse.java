package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Reports whether a user is enrolled and, crucially, until when.
 *
 * <p>The reference image is hard-deleted by S3 lifecycle {@code retentionDays}
 * after enrolment, while the record outlives it by a grace period.
 * {@code expiresAt} and {@code expired} are what let an integrator run a
 * re-enrolment sweep instead of discovering the gap as a 422 mid-signature —
 * and the sweep has to happen inside that grace window, because once it passes
 * this route answers 404, which is indistinguishable from "never enrolled".
 */
public class EnrollmentStatusResponse {

    @SerializedName("userExternalId")
    private String userExternalId;

    @SerializedName("enrollmentSource")
    private String enrollmentSource;

    @SerializedName("enrollmentVersion")
    private Integer enrollmentVersion;

    @SerializedName("enrollmentHash")
    private String enrollmentHash;

    @SerializedName("enrolledAt")
    private String enrolledAt;

    /** When the reference image is deleted. */
    @SerializedName("expiresAt")
    private String expiresAt;

    /** True once {@code expiresAt} has passed — re-enrol. */
    @SerializedName("expired")
    private Boolean expired;

    @SerializedName("retentionDays")
    private Integer retentionDays;

    /** CPF is masked: this route is enumerable by userExternalId. */
    @SerializedName("maskedCpf")
    private String maskedCpf;

    @SerializedName("faceConfidence")
    private Double faceConfidence;

    @SerializedName("documentImageHash")
    private String documentImageHash;

    public EnrollmentStatusResponse() {
    }

    public String getUserExternalId() { return userExternalId; }
    public void setUserExternalId(String userExternalId) { this.userExternalId = userExternalId; }

    public String getEnrollmentSource() { return enrollmentSource; }
    public void setEnrollmentSource(String enrollmentSource) { this.enrollmentSource = enrollmentSource; }

    public Integer getEnrollmentVersion() { return enrollmentVersion; }
    public void setEnrollmentVersion(Integer enrollmentVersion) { this.enrollmentVersion = enrollmentVersion; }

    public String getEnrollmentHash() { return enrollmentHash; }
    public void setEnrollmentHash(String enrollmentHash) { this.enrollmentHash = enrollmentHash; }

    public String getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(String enrolledAt) { this.enrolledAt = enrolledAt; }

    public String getExpiresAt() { return expiresAt; }
    public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }

    public Boolean getExpired() { return expired; }
    public void setExpired(Boolean expired) { this.expired = expired; }

    public Integer getRetentionDays() { return retentionDays; }
    public void setRetentionDays(Integer retentionDays) { this.retentionDays = retentionDays; }

    public String getMaskedCpf() { return maskedCpf; }
    public void setMaskedCpf(String maskedCpf) { this.maskedCpf = maskedCpf; }

    public Double getFaceConfidence() { return faceConfidence; }
    public void setFaceConfidence(Double faceConfidence) { this.faceConfidence = faceConfidence; }

    public String getDocumentImageHash() { return documentImageHash; }
    public void setDocumentImageHash(String documentImageHash) { this.documentImageHash = documentImageHash; }

    @Override
    public String toString() {
        return "EnrollmentStatusResponse{userExternalId='" + userExternalId
                + "', expiresAt='" + expiresAt + "', expired=" + expired + "}";
    }
}
