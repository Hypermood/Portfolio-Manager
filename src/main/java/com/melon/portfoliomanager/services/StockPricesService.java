package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.StockPricesDTO;
import com.melon.portfoliomanager.utils.KafkaMessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StockPricesService {
    private static final Logger logger = LoggerFactory.getLogger(StockPricesService.class);
    private final CompanyStocksManager companyStocksManager;
    private final StockPricesHttpService stockPricesHttpService;
    private final MessageBrokerService messageBrokerService;
    private final KafkaMessageUtils kafkaMessageUtils;

    @Autowired
    public StockPricesService(@Qualifier("stock-prices-mock-api") StockPricesHttpService stockPricesHttpService,
                              @Qualifier("kafka") MessageBrokerService messageBrokerService,
                              CompanyStocksManager companyStocksManager,
                              KafkaMessageUtils kafkaMessageUtils) {
        this.stockPricesHttpService = stockPricesHttpService;
        this.messageBrokerService = messageBrokerService;
        this.companyStocksManager = companyStocksManager;
        this.kafkaMessageUtils = kafkaMessageUtils;
    }

    @Scheduled(fixedDelay = 15 * 60 * 1000)
    private void fetchCompanyStocks() {
        try {
            ResponseEntity<?> stockPricesResponse = stockPricesHttpService.getStockPrices();
            StockPricesDTO latestCompanyStocks = (StockPricesDTO) stockPricesResponse.getBody();
            Map<String, Double> companyStocksWithPriceChanges =
                    companyStocksManager.getCompanyStocksWithExtremePriceChanges(latestCompanyStocks.getStockPrices());
            if (!companyStocksWithPriceChanges.isEmpty()) {
                String message = kafkaMessageUtils.prepareStockPriceChangesMessage(companyStocksWithPriceChanges);
                messageBrokerService.sendMessage(message);
            }
            companyStocksManager.updateCompanyStockPrices(latestCompanyStocks.getStockPrices());
        } catch (Exception e) {
            logger.error(String.format("Error while fetching data from stock prices API ! exception=%s", e));
        }
    }
}
