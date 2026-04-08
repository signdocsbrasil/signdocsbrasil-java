package com.signdocsbrasil.api.errors;

/**
 * Exception representing an API error response with RFC 7807 problem details.
 * Base class for all HTTP status-specific API exceptions.
 */
public class ApiException extends SignDocsBrasilException {

    private final int status;
    private final ProblemDetail problemDetail;

    public ApiException(ProblemDetail problemDetail) {
        super(problemDetail.getDetail() != null ? problemDetail.getDetail() : problemDetail.getTitle());
        this.status = problemDetail.getStatus();
        this.problemDetail = problemDetail;
    }

    /**
     * Returns the HTTP status code of the error response.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Returns the RFC 7807 problem detail object.
     */
    public ProblemDetail getProblemDetail() {
        return problemDetail;
    }

    /**
     * Returns the problem type URI.
     */
    public String getType() {
        return problemDetail.getType();
    }

    /**
     * Returns the problem title.
     */
    public String getTitle() {
        return problemDetail.getTitle();
    }

    /**
     * Returns the problem detail message.
     */
    public String getDetail() {
        return problemDetail.getDetail();
    }

    /**
     * Returns the problem instance URI.
     */
    public String getInstance() {
        return problemDetail.getInstance();
    }
}
