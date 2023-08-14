package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.StockPricesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class StockPricesService {
    private final CompanyStocksManager companyStocksManager = new CompanyStocksManager();
    private final StockPricesHttpService stockPricesHttpService;

    @Value("${stock.api.host}")
    private String stockApiHost;

    @Value("${stock.api.port}")
    private String stockApiPort;

    @Autowired
    public StockPricesService(StockPricesHttpService stockPricesHttpService) {
        this.stockPricesHttpService = stockPricesHttpService;
    }

    @Scheduled(fixedDelay = 15 * 60 * 1000)
    private void fetchCompanyStocks() {
        StockPricesDTO latestCompanyStocks = fetchLatestCompanyStocks();
        companyStocksManager.updateCompanyStockPrices(latestCompanyStocks.getStockPrices());
    }

    private StockPricesDTO fetchLatestCompanyStocks() {
        URI url = URI.create(String.format("http://%s:%s/stock/prices", stockApiHost, stockApiPort));
        ResponseEntity<StockPricesDTO> response = stockPricesHttpService.getStockPrices(url);
        return response.getBody();
    }
}
