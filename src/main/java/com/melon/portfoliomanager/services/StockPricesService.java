package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.StockPricesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockPricesService {
    private final double PRICE_CHANGE_PERCENT_THRESHOLD = 5;
    private final Map<String, Double> companyStocks = new HashMap<>();
    private final HttpService httpService;
    private final NotificationService notificationService;

    @Value("${stock.api.host}")
    private String stockApiHost;

    @Value("${stock.api.port}")
    private String stockApiPort;

    @Autowired
    public StockPricesService(HttpService httpService, NotificationService notificationService) {
        this.httpService = httpService;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelay = 15 * 60 * 1000)
    private void notifyUserAboutExtremePriceChanges() {
        StockPricesDTO latestCompanyStocks = fetchLatestCompanyStocks();
        Map<String, Double> companyStocksWithExtremeChanges =
                getCompanyStocksWithExtremePriceChanges(latestCompanyStocks.getStockPrices());
        notificationService.notifyUser(companyStocksWithExtremeChanges);
        updateCompanyStockPrices(latestCompanyStocks.getStockPrices());
    }

    private StockPricesDTO fetchLatestCompanyStocks() {
        URI url = URI.create(String.format("http://%s:%s/stock/prices", stockApiHost, stockApiPort));
        ResponseEntity<StockPricesDTO> response = httpService.getStockPrices(url);
        return response.getBody();
    }

    private Map<String, Double> getCompanyStocksWithExtremePriceChanges(Map<String, Double> latestCompanyStocks) {
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
        if (latestStockPrice > lastStockPrice) {
            double percentIncrease = ((latestStockPrice - lastStockPrice) / lastStockPrice) * 100;
            return percentIncrease > PRICE_CHANGE_PERCENT_THRESHOLD;
        }

        double percentIncrease = ((lastStockPrice - latestStockPrice) / lastStockPrice) * 100;
        return percentIncrease > PRICE_CHANGE_PERCENT_THRESHOLD;
    }

    private void updateCompanyStockPrices(Map<String, Double> latestCompanyStocks) {
        companyStocks.clear();
        companyStocks.putAll(latestCompanyStocks);
    }
}
