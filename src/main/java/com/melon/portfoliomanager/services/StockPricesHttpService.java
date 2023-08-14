package com.melon.portfoliomanager.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class StockPricesHttpService {
    protected WebClient webClient;

    @Value("${stock.api.host}")
    protected String stockApiHost;

    @Value("${stock.api.port}")
    protected String stockApiPort;

    public StockPricesHttpService(WebClient webClient) {
        this.webClient = webClient;
    }

    public abstract ResponseEntity<?> getStockPrices();
}
