package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.EnrollUserRequest;
import com.signdocsbrasil.api.models.EnrollUserResponse;
import com.signdocsbrasil.api.models.EnrollmentStatusResponse;
import com.signdocsbrasil.api.models.DeleteEnrollmentResponse;

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

    /**
     * Reads whether a user is enrolled and, crucially, until when.
     *
     * <p>Use it to sweep your user base and re-enrol before {@code expired}
     * flips. Nothing warns you on its own beyond the
     * {@code ENROLLMENT.EXPIRING} webhook, and once the grace window closes
     * this throws NotFound rather than reporting an expired enrolment.
     *
     * @param userExternalId the external user ID
     * @return the enrollment status, including the expiry window
     */
    public EnrollmentStatusResponse getEnrollment(String userExternalId) {
        return http.request("GET", "/v1/users/" + userExternalId + "/enrollment",
                null, EnrollmentStatusResponse.class);
    }

    /**
     * Reads the enrollment status with a per-request timeout.
     *
     * @param userExternalId the external user ID
     * @param timeout        the request timeout
     * @return the enrollment status, including the expiry window
     */
    public EnrollmentStatusResponse getEnrollment(String userExternalId, Duration timeout) {
        return http.request("GET", "/v1/users/" + userExternalId + "/enrollment",
                null, EnrollmentStatusResponse.class, timeout);
    }

    /**
     * Erases a user's biometric enrolment (LGPD art. 18).
     *
     * <p>Destroys every stored version of the reference image, not just the
     * current one, and removes the record. Irreversible.
     *
     * @param userExternalId the external user ID
     * @return what was destroyed
     */
    public DeleteEnrollmentResponse deleteEnrollment(String userExternalId) {
        return http.request("DELETE", "/v1/users/" + userExternalId + "/enrollment",
                null, DeleteEnrollmentResponse.class);
    }

    /**
     * Erases a user's biometric enrolment with a per-request timeout.
     *
     * @param userExternalId the external user ID
     * @param timeout        the request timeout
     * @return what was destroyed
     */
    public DeleteEnrollmentResponse deleteEnrollment(String userExternalId, Duration timeout) {
        return http.request("DELETE", "/v1/users/" + userExternalId + "/enrollment",
                null, DeleteEnrollmentResponse.class, timeout);
    }
}
