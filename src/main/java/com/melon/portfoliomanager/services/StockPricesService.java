package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.StockPricesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StockPricesService {
    private static final Logger logger = LoggerFactory.getLogger(StockPricesService.class);
    private final CompanyStocksManager companyStocksManager;
    private final StockPricesHttpService stockPricesHttpService;

    @Autowired
    public StockPricesService(@Qualifier("stock-prices-mock-api") StockPricesHttpService stockPricesHttpService,
                              CompanyStocksManager companyStocksManager) {
        this.stockPricesHttpService = stockPricesHttpService;
        this.companyStocksManager = companyStocksManager;
    }

    @Scheduled(fixedDelay = 15 * 60 * 1000)
    private void fetchCompanyStocks() {
        try {
            ResponseEntity<?> stockPricesResponse = stockPricesHttpService.getStockPrices();
            StockPricesDTO latestCompanyStocks = (StockPricesDTO) stockPricesResponse.getBody();
            companyStocksManager.updateCompanyStockPrices(latestCompanyStocks.getStockPrices());
        } catch (Exception e) {
            logger.error(String.format("Error while fetching data from stock prices API ! exception=%s", e));
        }
    }
}
