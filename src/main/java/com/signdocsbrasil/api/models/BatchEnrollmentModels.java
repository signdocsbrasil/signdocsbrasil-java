package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Container for the batch-enrollment model types.
 *
 * <p>Grouped in one file because they are only ever used together, as the
 * request and response of {@code POST /v1/users/enrollments}.
 */
public final class BatchEnrollmentModels {

    private BatchEnrollmentModels() {
    }

    /** Advisory reasons a photo is usable but weak. */
    public static final String WARNING_LOW_BRIGHTNESS = "LOW_BRIGHTNESS";
    public static final String WARNING_LOW_SHARPNESS = "LOW_SHARPNESS";
    public static final String WARNING_FACE_TOO_SMALL = "FACE_TOO_SMALL";
    public static final String WARNING_HEAD_TURNED = "HEAD_TURNED";

    /** One row of a batch enrollment. */
    public static class Item {

        @SerializedName("userExternalId")
        private String userExternalId;

        @SerializedName("image")
        private String image;

        @SerializedName("cpf")
        private String cpf;

        @SerializedName("source")
        private String source;

        public Item() {
        }

        public Item(String userExternalId, String image, String cpf) {
            this.userExternalId = userExternalId;
            this.image = image;
            this.cpf = cpf;
        }

        public String getUserExternalId() { return userExternalId; }
        public void setUserExternalId(String userExternalId) { this.userExternalId = userExternalId; }

        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }

        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }

        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
    }

    /** Request body for {@code POST /v1/users/enrollments}. */
    public static class Request {

        @SerializedName("enrollments")
        private List<Item> enrollments;

        /**
         * Inspect without writing. Every row is evaluated and returned with
         * quality metrics, and <strong>nothing is persisted</strong> — no image
         * reaches storage, no record is created, and the 90-day retention clock
         * never starts.
         *
         * <p>Rekognition's confidence answers "is this a face?", not "is this a
         * good reference": a dark, blurred photo enrols happily at 99.99
         * confidence and then fails face matching months later, one employee at
         * a time. A dry run surfaces that while the batch is still in front of
         * you. It costs the same one Rekognition call per row that enrolling
         * would.
         */
        @SerializedName("dryRun")
        private Boolean dryRun;

        public Request() {
        }

        public Request(List<Item> enrollments) {
            this.enrollments = enrollments;
        }

        public List<Item> getEnrollments() { return enrollments; }
        public void setEnrollments(List<Item> enrollments) { this.enrollments = enrollments; }

        public Boolean getDryRun() { return dryRun; }
        public void setDryRun(Boolean dryRun) { this.dryRun = dryRun; }
    }

    /** Rekognition's 0-100 measures for the detected face. Dry run only. */
    public static class Quality {

        @SerializedName("brightness")
        private Double brightness;

        @SerializedName("sharpness")
        private Double sharpness;

        public Double getBrightness() { return brightness; }
        public Double getSharpness() { return sharpness; }
    }

    /** Head rotation in degrees. Dry run only. */
    public static class Pose {

        @SerializedName("yaw")
        private Double yaw;

        @SerializedName("pitch")
        private Double pitch;

        @SerializedName("roll")
        private Double roll;

        public Double getYaw() { return yaw; }
        public Double getPitch() { return pitch; }
        public Double getRoll() { return roll; }
    }

    /** One row's outcome. */
    public static class Result {

        @SerializedName("index")
        private Integer index;

        @SerializedName("userExternalId")
        private String userExternalId;

        /**
         * {@code enrolled}/{@code failed} on a real write;
         * {@code usable}/{@code marginal}/{@code rejected} on a dry run.
         *
         * <p>{@code marginal} is the one to act on: it would enrol without
         * complaint today and is exactly what becomes a rejected signature later.
         */
        @SerializedName("status")
        private String status;

        @SerializedName("error")
        private String error;

        @SerializedName("enrollmentVersion")
        private Integer enrollmentVersion;

        @SerializedName("expiresAt")
        private String expiresAt;

        @SerializedName("faceConfidence")
        private Double faceConfidence;

        @SerializedName("quality")
        private Quality quality;

        @SerializedName("pose")
        private Pose pose;

        /** Face area as a fraction of the frame, 0-1. Dry run only. */
        @SerializedName("faceCoverage")
        private Double faceCoverage;

        /** Empty on a clean photo. Dry run only. */
        @SerializedName("warnings")
        private List<String> warnings;

        public Integer getIndex() { return index; }
        public String getUserExternalId() { return userExternalId; }
        public String getStatus() { return status; }
        public String getError() { return error; }
        public Integer getEnrollmentVersion() { return enrollmentVersion; }
        public String getExpiresAt() { return expiresAt; }
        public Double getFaceConfidence() { return faceConfidence; }
        public Quality getQuality() { return quality; }
        public Pose getPose() { return pose; }
        public Double getFaceCoverage() { return faceCoverage; }
        public List<String> getWarnings() { return warnings; }
    }

    /**
     * Result of a batch enrollment.
     *
     * <p>Partial success is the point, so this comes back {@code 200} even when
     * rows failed: one unusable photo must not reject the other twenty-four.
     * Read {@code results}, not the HTTP status.
     */
    public static class Response {

        @SerializedName("submitted")
        private Integer submitted;

        @SerializedName("succeeded")
        private Integer succeeded;

        @SerializedName("failed")
        private Integer failed;

        @SerializedName("dryRun")
        private Boolean dryRun;

        @SerializedName("usable")
        private Integer usable;

        @SerializedName("marginal")
        private Integer marginal;

        @SerializedName("rejected")
        private Integer rejected;

        @SerializedName("results")
        private List<Result> results;

        public Integer getSubmitted() { return submitted; }
        public Integer getSucceeded() { return succeeded; }
        public Integer getFailed() { return failed; }
        public Boolean getDryRun() { return dryRun; }
        public Integer getUsable() { return usable; }
        public Integer getMarginal() { return marginal; }
        public Integer getRejected() { return rejected; }
        public List<Result> getResults() { return results; }
    }
}
