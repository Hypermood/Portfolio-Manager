package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.AnalyticsResponseDto;
import com.melon.portfoliomanager.dtos.responses.AssetStatDto;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.models.PortfolioItem;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.PortfolioItemRepository;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
public class AnalyticsServiceTest {

    @MockBean
    private PortfolioItemRepository portfolioItemRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CompanyStocksManager companyStocksManager;

    @Autowired
    private AnalyticsService analyticsService;

    @Test
    public void fetchAnalyticsForUser_NonExistentUser_ThrowsException() {
        when(userRepository.findByUsername("test1")).thenReturn(Collections.emptyList());

        assertThrows(NoSuchUserException.class, () -> {
            analyticsService.fetchAnalyticsForUser("test1");
        });
    }

    @Test
    public void fetchAnalyticsForUser_UserExistsButNoPortfolioItems_ReturnsZeroValues() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findByUsername("testUser")).thenReturn(Collections.singletonList(user));
        when(portfolioItemRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        AnalyticsResponseDto response = analyticsService.fetchAnalyticsForUser("testUser");

        assertNotNull(response);
        assertEquals(0, response.getTotalPortfolioValue());
        assertEquals(0, response.getTotalPortfolioGainVal());
        assertEquals(0, response.getTotalPortfolioGainPct());
        assertTrue(response.getAssets().isEmpty());
    }


    @Test
    public void fetchAnalyticsForUser_ValidScenario_ReturnsCorrectValues() {
        User user = new User();
        user.setId(1L);

        PortfolioItem item = new PortfolioItem();
        item.setCompanyName("Apple");
        item.setQuantity(5.0);
        item.setTotalBoughtPrice(400.0);
        item.setTotalSoldPrice(0.0);

        Map<String, Double> stockPrices = new HashMap<>();
        stockPrices.put("Apple", 100.0);

        when(userRepository.findByUsername("testUser")).thenReturn(Collections.singletonList(user));
        when(portfolioItemRepository.findByUserId(1L)).thenReturn(Collections.singletonList(item));
        when(companyStocksManager.getCompanyStocks()).thenReturn(stockPrices);

        AnalyticsResponseDto response = analyticsService.fetchAnalyticsForUser("testUser");

        assertNotNull(response);
        assertEquals(500, response.getTotalPortfolioValue());
        assertEquals(100, response.getTotalPortfolioGainVal());

    }

    @Test
    public void fetchAnalyticsForUser_MultipleAssets_ReturnsCorrectValues() throws Exception {
        User user = new User();
        user.setId(1L);

        PortfolioItem efItem = new PortfolioItem("Efbet", 5.0, 400.0, 100.0);

        PortfolioItem winItem = new PortfolioItem("Winbet", 10.0, 1000.0, 234.0);

        PortfolioItem inItem = new PortfolioItem("Inbet", 2.0, 2000.0, 0.0);

        Map<String, Double> stockPrices = new HashMap<>();
        stockPrices.put("Efbet", 100.0);
        stockPrices.put("Winbet", 110.0);
        stockPrices.put("Inbet", 1050.0);

        when(userRepository.findByUsername("testUser")).thenReturn(Collections.singletonList(user));
        when(portfolioItemRepository.findByUserId(1L)).thenReturn(Arrays.asList(efItem, winItem, inItem));
        when(companyStocksManager.getCompanyStocks()).thenReturn(stockPrices);

        AnalyticsResponseDto response = analyticsService.fetchAnalyticsForUser("testUser");

        // Assertions for the AnalyticsResponse
        assertEquals("testUser", response.getUsername());
        assertEquals(3700.0, response.getTotalPortfolioValue());

        // Calculated from the gain values of each portfolio item
        double expectedTotalGain = 200.0 + 334.0 + 100.0;
        assertEquals(expectedTotalGain, response.getTotalPortfolioGainVal());

        // Gain Percentage = (totalGain / totalInvested) * 100
        double totalInvested = 400.0 + 1000.0 + 2000.0;
        double expectedTotalGainPct = (expectedTotalGain / totalInvested) * 100;
        assertEquals(expectedTotalGainPct, response.getTotalPortfolioGainPct(), 0.1);

        // Validate individual assets
        List<AssetStatDto> assets = response.getAssets();
        assertEquals(3, assets.size());

        // Efbet assertions
        AssetStatDto efStat = assets.stream().filter(a -> "Efbet".equals(a.getCompany())).findFirst().orElse(null);
        assertNotNull(efStat);
        assertEquals(50.0, efStat.getGainPct());
        assertEquals(200.0, efStat.getGainVal());
        assertEquals(500.0 / 3700.0, efStat.getAllocation(), 0.1);

        // Winbet assertions
        AssetStatDto winStat = assets.stream().filter(a -> "Winbet".equals(a.getCompany())).findFirst().orElse(null);
        assertNotNull(winStat);
        assertEquals(33.4, winStat.getGainPct());
        assertEquals(334.0, winStat.getGainVal());
        assertEquals(1100.0 / 3700.0, winStat.getAllocation(), 0.1);

        // Inbet assertions
        AssetStatDto inStat = assets.stream().filter(a -> "Inbet".equals(a.getCompany())).findFirst().orElse(null);
        assertNotNull(inStat);
        assertEquals(5.0, inStat.getGainPct());
        assertEquals(100.0, inStat.getGainVal());
        assertEquals(2100.0 / 3700.0, inStat.getAllocation(), 0.1);
    }


}
