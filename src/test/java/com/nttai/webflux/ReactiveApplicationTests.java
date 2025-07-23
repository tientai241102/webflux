package com.nttai.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReactiveApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetDataSuccess() {
        webTestClient.get()
                .uri("/api/data")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.meta.code").isEqualTo(200000)
                .jsonPath("$.meta.message").isEqualTo("Success")
                .jsonPath("$.data").exists();
    }

    @Test
    void testGetDataError() {
        // Simulate error by mocking or using a test profile if possible
        // Here, we check for a known error structure (e.g., NO_POST)
        // This test may need to be adjusted based on your test data
        webTestClient.get()
                .uri("/api/data?forceError=true") // You may need to implement this param for testing
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.meta.code").exists()
                .jsonPath("$.meta.message").exists()
                .jsonPath("$.meta.errors").doesNotExist(); // Adjust if you expect errors array
    }
}
