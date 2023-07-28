package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.StockPricesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Service
public class HttpService {
    private static final Logger logger = LoggerFactory.getLogger(HttpService.class);
    private final WebClient webClient;

    @Autowired
    public HttpService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ResponseEntity<StockPricesDTO> getStockPrices(URI uri) {
        ResponseEntity<StockPricesDTO> response = webClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(StockPricesDTO.class)
                .block();
        handleError(response);
        return response;
    }

    private void handleError(ResponseEntity<StockPricesDTO> response) {
        String errorMessage = getErrorMessage(response);
        if (errorMessage == null) {
            return;
        }
        logger.error(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    private String getErrorMessage(ResponseEntity<StockPricesDTO> response) {
        if (response == null) {
            return "Error while fetching data from stock prices API ! Response is null !";
        }

        if (response.getStatusCode().is4xxClientError()) {
            return "Bad request to stock prices mock API !";
        }

        if (response.getStatusCode().is5xxServerError()) {
            return "Internal server error during call to stock prices mock API";
        }

        if (response.getBody() == null) {
            return "Error while fetching data from stock prices API ! Response body is null !";
        }

        return response.getBody().getError();
    }
}
