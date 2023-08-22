package com.melon.portfoliomanager.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CompanyStocksManager {
    private final Map<String, Double> companyStocks = new HashMap<>();

    public void updateCompanyStockPrices(Map<String, Double> latestCompanyStocks) {
        companyStocks.clear();
        companyStocks.putAll(latestCompanyStocks);
    }

    public Map<String, Double> getCompanyStocks() {
        return new HashMap<>(companyStocks);
    }
}
