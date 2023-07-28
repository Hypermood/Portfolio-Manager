package com.melon.portfoliomanager.dtos.responses;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StockPricesDTO {
    private Map<String, Double> stockPrices;
    private String error;

    public StockPricesDTO() {
    }

    public StockPricesDTO(Map<String, Double> stockPrices) {
        this.stockPrices = new HashMap<>(stockPrices);
    }

    public StockPricesDTO(String error) {
        this.error = error;
    }

    public Map<String, Double> getStockPrices() {
        if (stockPrices == null) {
            return stockPrices;
        }
        return Collections.unmodifiableMap(stockPrices);
    }

    public String getError() {
        return error;
    }

    public void setStockPrices(Map<String, Double> stockPrices) {
        if (stockPrices == null) {
            this.stockPrices = null;
            return;
        }
        this.stockPrices = new HashMap<>(stockPrices);
    }

    public void setError(String error) {
        this.error = error;
    }
}
