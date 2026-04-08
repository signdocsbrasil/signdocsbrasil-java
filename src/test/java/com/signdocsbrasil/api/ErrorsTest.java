package com.signdocsbrasil.api;

import com.signdocsbrasil.api.errors.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ErrorsTest {

    @Test
    void problemDetailFallback() {
        ProblemDetail pd = ProblemDetail.fallback(400, "bad input");
        assertTrue(pd.getType().contains("errors/400"));
        assertEquals("HTTP 400", pd.getTitle());
        assertEquals(400, pd.getStatus());
        assertEquals("bad input", pd.getDetail());
    }

    @Test
    void problemDetailFullConstructor() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Bad Request", 400, "Invalid input", "/v1/test");
        assertEquals("about:blank", pd.getType());
        assertEquals("Bad Request", pd.getTitle());
        assertEquals(400, pd.getStatus());
        assertEquals("Invalid input", pd.getDetail());
        assertEquals("/v1/test", pd.getInstance());
    }

    @Test
    void apiExceptionGetters() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Not Found", 404, "Resource not found", null);
        ApiException ex = new ApiException(pd);

        assertEquals(404, ex.getStatus());
        assertEquals("about:blank", ex.getType());
        assertEquals("Not Found", ex.getTitle());
        assertEquals("Resource not found", ex.getDetail());
        assertNull(ex.getInstance());
        assertNotNull(ex.getProblemDetail());
    }

    @Test
    void apiExceptionUsesDetailAsMessage() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Title", 400, "Detail message", null);
        ApiException ex = new ApiException(pd);
        assertEquals("Detail message", ex.getMessage());
    }

    @Test
    void apiExceptionUsesTitleWhenNoDetail() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Title Only", 400, null, null);
        ApiException ex = new ApiException(pd);
        assertEquals("Title Only", ex.getMessage());
    }

    @Test
    void rateLimitExceptionRetryAfter() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Rate Limited", 429, null, null);
        RateLimitException ex = new RateLimitException(pd, 5);
        assertEquals(429, ex.getStatus());
        assertEquals(5, ex.getRetryAfterSeconds());
    }

    @Test
    void rateLimitExceptionNullRetryAfter() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Rate Limited", 429, null, null);
        RateLimitException ex = new RateLimitException(pd, null);
        assertNull(ex.getRetryAfterSeconds());
    }

    @Test
    void badRequestException() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Bad Request", 400, null, null);
        BadRequestException ex = new BadRequestException(pd);
        assertEquals(400, ex.getStatus());
        assertInstanceOf(ApiException.class, ex);
    }

    @Test
    void unauthorizedException() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Unauthorized", 401, null, null);
        UnauthorizedException ex = new UnauthorizedException(pd);
        assertEquals(401, ex.getStatus());
    }

    @Test
    void notFoundException() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Not Found", 404, null, null);
        NotFoundException ex = new NotFoundException(pd);
        assertEquals(404, ex.getStatus());
    }

    @Test
    void conflictException() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Conflict", 409, null, null);
        ConflictException ex = new ConflictException(pd);
        assertEquals(409, ex.getStatus());
    }

    @Test
    void internalServerException() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Internal Server Error", 500, null, null);
        InternalServerException ex = new InternalServerException(pd);
        assertEquals(500, ex.getStatus());
    }

    @Test
    void serviceUnavailableException() {
        ProblemDetail pd = new ProblemDetail("about:blank", "Service Unavailable", 503, null, null);
        ServiceUnavailableException ex = new ServiceUnavailableException(pd);
        assertEquals(503, ex.getStatus());
    }
}
