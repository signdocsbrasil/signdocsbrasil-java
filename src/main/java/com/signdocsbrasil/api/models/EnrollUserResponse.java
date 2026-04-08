package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from enrolling a user.
 */
public class EnrollUserResponse {

    @SerializedName("userExternalId")
    private String userExternalId;

    @SerializedName("enrollmentHash")
    private String enrollmentHash;

    @SerializedName("enrollmentVersion")
    private int enrollmentVersion;

    @SerializedName("enrollmentSource")
    private String enrollmentSource;

    @SerializedName("enrolledAt")
    private String enrolledAt;

    @SerializedName("cpf")
    private String cpf;

    @SerializedName("faceConfidence")
    private Double faceConfidence;

    @SerializedName("documentImageHash")
    private String documentImageHash;

    @SerializedName("extractionConfidence")
    private Double extractionConfidence;

    public EnrollUserResponse() {
    }

    public String getUserExternalId() {
        return userExternalId;
    }

    public void setUserExternalId(String userExternalId) {
        this.userExternalId = userExternalId;
    }

    public String getEnrollmentHash() {
        return enrollmentHash;
    }

    public void setEnrollmentHash(String enrollmentHash) {
        this.enrollmentHash = enrollmentHash;
    }

    public int getEnrollmentVersion() {
        return enrollmentVersion;
    }

    public void setEnrollmentVersion(int enrollmentVersion) {
        this.enrollmentVersion = enrollmentVersion;
    }

    public String getEnrollmentSource() {
        return enrollmentSource;
    }

    public void setEnrollmentSource(String enrollmentSource) {
        this.enrollmentSource = enrollmentSource;
    }

    public String getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(String enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Double getFaceConfidence() {
        return faceConfidence;
    }

    public void setFaceConfidence(Double faceConfidence) {
        this.faceConfidence = faceConfidence;
    }

    public String getDocumentImageHash() {
        return documentImageHash;
    }

    public void setDocumentImageHash(String documentImageHash) {
        this.documentImageHash = documentImageHash;
    }

    public Double getExtractionConfidence() {
        return extractionConfidence;
    }

    public void setExtractionConfidence(Double extractionConfidence) {
        this.extractionConfidence = extractionConfidence;
    }
}
