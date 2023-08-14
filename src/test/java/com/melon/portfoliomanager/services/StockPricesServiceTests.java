package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.StockPricesDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StockPricesServiceTests {
    @org.springframework.boot.test.context.TestConfiguration
    public static class TestConfiguration {
        @Bean
        @Primary
        public StockPricesHttpService stockPricesHttpServiceMock() {
            StockPricesHttpService stockPricesHttpService = mock(StockPricesHttpService.class);
            StockPricesDTO stockPricesDTO = new StockPricesDTO(Map.of("IBM", 1.23, "FB", 2.34));
            doReturn(ResponseEntity.status(HttpStatus.OK).body(stockPricesDTO)).when(stockPricesHttpService).getStockPrices(any());
            return stockPricesHttpService;
        }
    }

    @Autowired
    private StockPricesHttpService stockPricesHttpService;

    @Test
    public void testNotifyUserAboutExtremePriceChangesWillSucceed() {
        verify(stockPricesHttpService, times(1)).getStockPrices(any());
    }
}
