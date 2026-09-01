package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Result of erasing a user's biometric enrolment (LGPD art. 18).
 */
public class DeleteEnrollmentResponse {

    @SerializedName("userExternalId")
    private String userExternalId;

    @SerializedName("deleted")
    private Boolean deleted;

    @SerializedName("deletedAt")
    private String deletedAt;

    @SerializedName("enrollmentVersion")
    private Integer enrollmentVersion;

    /** Objects removed from storage; every version of each is destroyed. */
    @SerializedName("objectsDeleted")
    private Integer objectsDeleted;

    @SerializedName("versionsDeleted")
    private Integer versionsDeleted;

    public DeleteEnrollmentResponse() {
    }

    public String getUserExternalId() { return userExternalId; }
    public void setUserExternalId(String userExternalId) { this.userExternalId = userExternalId; }

    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public String getDeletedAt() { return deletedAt; }
    public void setDeletedAt(String deletedAt) { this.deletedAt = deletedAt; }

    public Integer getEnrollmentVersion() { return enrollmentVersion; }
    public void setEnrollmentVersion(Integer enrollmentVersion) { this.enrollmentVersion = enrollmentVersion; }

    public Integer getObjectsDeleted() { return objectsDeleted; }
    public void setObjectsDeleted(Integer objectsDeleted) { this.objectsDeleted = objectsDeleted; }

    public Integer getVersionsDeleted() { return versionsDeleted; }
    public void setVersionsDeleted(Integer versionsDeleted) { this.versionsDeleted = versionsDeleted; }

    @Override
    public String toString() {
        return "DeleteEnrollmentResponse{userExternalId='" + userExternalId
                + "', deleted=" + deleted + ", versionsDeleted=" + versionsDeleted + "}";
    }
}
