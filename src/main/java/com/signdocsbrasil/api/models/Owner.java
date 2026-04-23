package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Identity of the requester creating a signing session or envelope,
 * distinct from the signer(s). When provided, SignDocs automatically:
 *
 * <ol>
 *   <li>Emails each signer an invitation with their signing URL — when
 *       {@code signer.email} differs from {@code owner.email}
 *       (case-insensitive).</li>
 *   <li>Emails the owner a completion notification per signer completion
 *       (and a final "all signed" message for envelopes).</li>
 * </ol>
 *
 * <p>Omit {@code owner} to keep the traditional behavior: the caller
 * delivers signing URLs via their own channels and relies on webhooks
 * for completion state.
 */
public class Owner {

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    public Owner() {
    }

    public Owner(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
