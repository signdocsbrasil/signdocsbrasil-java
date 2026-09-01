package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request to enroll a user with biometric reference data.
 */
public class EnrollUserRequest {

    @SerializedName("image")
    private String image;

    @SerializedName("cpf")
    private String cpf;

    @SerializedName("source")
    private String source;

    public EnrollUserRequest() {
    }

    /**
     * Creates an enrollment request with the required fields.
     *
     * @param image base64 encoded JPEG image
     * @param cpf   11 digits, no punctuation
     */
    public EnrollUserRequest(String image, String cpf) {
        this.image = image;
        this.cpf = cpf;
        this.source = "BANK_PROVIDED";
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Inspect without writing. Returns the same verdict the batch endpoint
     * gives and persists nothing — no image, no record, and the 90-day
     * retention clock never starts.
     */
    @SerializedName("dryRun")
    private Boolean dryRun;

    public Boolean getDryRun() { return dryRun; }
    public void setDryRun(Boolean dryRun) { this.dryRun = dryRun; }
}
