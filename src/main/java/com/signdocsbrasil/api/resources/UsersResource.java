package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.EnrollUserRequest;
import com.signdocsbrasil.api.models.EnrollUserResponse;
import com.signdocsbrasil.api.models.EnrollmentStatusResponse;
import com.signdocsbrasil.api.models.DeleteEnrollmentResponse;
import com.signdocsbrasil.api.models.BatchEnrollmentModels;

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

    /**
     * Enrols up to 25 users in one request.
     *
     * <p>The documented cap is 25 rows, but the binding limit is the request
     * body — roughly 6MB, and base64 inflates each photo by a third. Keep
     * photos under ~175KB (640x640 is ample) to use all 25 slots.
     *
     * <p>Set {@code dryRun} on the request to inspect the photos without
     * storing anything.
     *
     * @param request the rows to enrol
     * @return per-row outcomes; read them rather than the HTTP status
     */
    public BatchEnrollmentModels.Response enrollBatch(BatchEnrollmentModels.Request request) {
        return http.request("POST", "/v1/users/enrollments",
                request, BatchEnrollmentModels.Response.class);
    }

    /**
     * Enrols a batch with a per-request timeout.
     *
     * @param request the rows to enrol
     * @param timeout the request timeout
     * @return per-row outcomes
     */
    public BatchEnrollmentModels.Response enrollBatch(BatchEnrollmentModels.Request request, Duration timeout) {
        return http.request("POST", "/v1/users/enrollments",
                request, BatchEnrollmentModels.Response.class, timeout);
    }
}
