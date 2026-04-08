package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response from listing signing sessions, with pagination support.
 */
public class SigningSessionListResponse {

    @SerializedName("sessions")
    private List<SigningSession> sessions;

    @SerializedName("nextToken")
    private String nextToken;

    @SerializedName("count")
    private int count;

    public SigningSessionListResponse() {
    }

    public List<SigningSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<SigningSession> sessions) {
        this.sessions = sessions;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "SigningSessionListResponse{count=" + count + ", hasNextToken=" + (nextToken != null) + "}";
    }
}
