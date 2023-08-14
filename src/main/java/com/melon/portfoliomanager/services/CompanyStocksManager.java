package com.melon.portfoliomanager.services;

import java.util.HashMap;
import java.util.Map;

public class CompanyStocksManager {
    private final Map<String, Double> companyStocks = new HashMap<>();

    public void updateCompanyStockPrices(Map<String, Double> latestCompanyStocks) {
        companyStocks.clear();
        companyStocks.putAll(latestCompanyStocks);
    }
}
