package com.melon.portfoliomanager.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompanyStocksManager {
    private final double PRICE_CHANGE_PERCENT_THRESHOLD = 5;
    private final Map<String, Double> companyStocks = new HashMap<>();

    public Map<String, Double> getCompanyStocksWithExtremePriceChanges(Map<String, Double> latestCompanyStocks) {
        return companyStocks.entrySet()
                .stream()
                .filter(entry -> {
                    double latestStockPrice = latestCompanyStocks.get(entry.getKey());
                    double lastStockPrice = entry.getValue();
                    return isPriceChangeAboveThreshold(latestStockPrice, lastStockPrice);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isPriceChangeAboveThreshold(double latestStockPrice, double lastStockPrice) {
        double percentChange = (Math.abs(latestStockPrice - lastStockPrice) / lastStockPrice) * 100;
        return percentChange > PRICE_CHANGE_PERCENT_THRESHOLD;
    }


    public void updateCompanyStockPrices(Map<String, Double> latestCompanyStocks) {
        companyStocks.clear();
        companyStocks.putAll(latestCompanyStocks);
    }

    public Map<String, Double> getCompanyStocks() {
        return new HashMap<>(companyStocks);
    }
}
