package com.signdocsbrasil.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.Gson;
import com.signdocsbrasil.api.models.DownloadResponse;
import org.junit.jupiter.api.Test;

/** Detached-signature fields on DownloadResponse (non-PDF transactions). */
class DownloadResponseTest {

    private final Gson gson = new Gson();

    @Test
    void deserializesDetachedSignatureFields() {
        // Non-PDF transactions come back as documentFormat "generic" with a
        // detached CAdES signature instead of an embedded signedUrl.
        String json = "{\"transactionId\":\"tx_2\",\"expiresIn\":900,"
                + "\"documentFormat\":\"generic\","
                + "\"originalUrl\":\"https://s3.example.com/document.docx\","
                + "\"signatureUrl\":\"https://s3.example.com/signature.p7s\"}";

        DownloadResponse resp = gson.fromJson(json, DownloadResponse.class);

        assertEquals("generic", resp.getDocumentFormat());
        assertEquals("https://s3.example.com/signature.p7s", resp.getSignatureUrl());
        assertNull(resp.getSignedUrl());
    }

    @Test
    void leavesNewFieldsNullForAPdf() {
        String json = "{\"transactionId\":\"tx_1\",\"expiresIn\":900,"
                + "\"documentFormat\":\"pdf\","
                + "\"signedUrl\":\"https://s3.example.com/signed.pdf\"}";

        DownloadResponse resp = gson.fromJson(json, DownloadResponse.class);

        assertNull(resp.getSignatureUrl());
        assertEquals("pdf", resp.getDocumentFormat());
        assertEquals("https://s3.example.com/signed.pdf", resp.getSignedUrl());
    }
}
