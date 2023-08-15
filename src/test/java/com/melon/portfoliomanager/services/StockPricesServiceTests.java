package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.StockPricesDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
public class StockPricesServiceTests {
    @org.springframework.boot.test.context.TestConfiguration
    public static class TestConfiguration {
        @Bean
        @Qualifier("stock-prices-mock-api")
        @Primary
        public StockPricesHttpService stockPricesHttpServiceMock() {
            StockPricesHttpService stockPricesHttpService = mock(StockPricesHttpService.class);
            StockPricesDTO stockPricesDTO = new StockPricesDTO(Map.of("IBM", 1.23, "FB", 2.34));
            doReturn(ResponseEntity.status(HttpStatus.OK).body(stockPricesDTO))
                    .when(stockPricesHttpService).getStockPrices();
            return stockPricesHttpService;
        }

        @Bean
        @Qualifier("kafka")
        @Primary
        public MessageBrokerService messageBrokerServiceMock() {
            return mock(MessageBrokerService.class);
        }
    }

    @Autowired
    @Qualifier("stock-prices-mock-api")
    private StockPricesHttpService stockPricesHttpService;

    @Autowired
    @Qualifier("kafka")
    private MessageBrokerService messageBrokerService;

    @Test
    public void testFetchCompanyStocksWillSucceed() {
        verify(stockPricesHttpService, times(1)).getStockPrices();
        verify(messageBrokerService, times(1)).sendMessage(any(Map.class));
    }
}
