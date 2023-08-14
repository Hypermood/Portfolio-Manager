package com.melon.portfoliomanager;

import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.responses.AnalyticsResponse;
import com.melon.portfoliomanager.services.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
public class AnalyticsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AnalyticsService analyticsService;

    @Test
    public void testAnalyticsForUser_ValidUser_OKRequest() {
        AnalyticsResponse dummyAnalytics = new AnalyticsResponse();
        dummyAnalytics.setUsername("testUser");
        dummyAnalytics.setTotalPortfolioValue(1000.0);


        when(analyticsService.fetchAnalyticsForUser("testUser")).thenReturn(dummyAnalytics);

        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/analytics")
                        .queryParam("username", "testUser")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("testUser")
                .jsonPath("$.totalPortfolioValue").isEqualTo(1000.0);

    }

    @Test
    public void testAnalyticsForUser_NoSuchUser_BadRequest() {
        when(analyticsService.fetchAnalyticsForUser("invalidUser"))
                .thenThrow(new NoSuchUserException("User not found"));

        webTestClient.get().uri("/analytics?username=invalidUser")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testAnalyticsForUser_NoUserName_BadRequest() {
        webTestClient.get().uri("/analytics")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testAnalyticsForUser_UnexpectedError_InternalServerError(CapturedOutput capturedOutput) {
        when(analyticsService.fetchAnalyticsForUser("testUser"))
                .thenThrow(new RuntimeException("Unexpected error"));

        webTestClient.get().uri("/analytics?username=testUser")
                .exchange()
                .expectStatus().is5xxServerError();

        assertTrue(capturedOutput.getOut().contains("Unexpected error"));
    }
}