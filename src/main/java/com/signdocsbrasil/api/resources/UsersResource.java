package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.EnrollUserRequest;
import com.signdocsbrasil.api.models.EnrollUserResponse;

import java.time.Duration;

/**
 * Resource for user management operations.
 */
public final class UsersResource {

    private final HttpClient http;

    public UsersResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Enrolls a user with biometric reference data.
     *
     * @param userExternalId the external user ID
     * @param request        the enrollment request with reference image
     * @return the enrollment response
     */
    public EnrollUserResponse enroll(String userExternalId, EnrollUserRequest request) {
        return http.request("PUT", "/v1/users/" + userExternalId + "/enrollment",
                request, EnrollUserResponse.class);
    }

    /**
     * Enrolls a user with biometric reference data with a per-request timeout.
     *
     * @param userExternalId the external user ID
     * @param request        the enrollment request with reference image
     * @param timeout        the request timeout
     * @return the enrollment response
     */
    public EnrollUserResponse enroll(String userExternalId, EnrollUserRequest request, Duration timeout) {
        return http.request("PUT", "/v1/users/" + userExternalId + "/enrollment",
                request, EnrollUserResponse.class, timeout);
    }
}
