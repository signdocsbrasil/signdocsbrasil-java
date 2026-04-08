package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a document signer.
 */
public class Signer {

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("userExternalId")
    private String userExternalId;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("cpf")
    private String cpf;

    @SerializedName("cnpj")
    private String cnpj;

    @SerializedName("phone")
    private String phone;

    @SerializedName("birthDate")
    private String birthDate;

    @SerializedName("otpChannel")
    private String otpChannel;

    public Signer() {
    }

    public Signer(String name, String userExternalId) {
        this.name = name;
        this.userExternalId = userExternalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserExternalId() {
        return userExternalId;
    }

    public void setUserExternalId(String userExternalId) {
        this.userExternalId = userExternalId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getOtpChannel() {
        return otpChannel;
    }

    public void setOtpChannel(String otpChannel) {
        this.otpChannel = otpChannel;
    }

    @Override
    public String toString() {
        return "Signer{name='" + name + "', userExternalId='" + userExternalId + "'}";
    }
}
