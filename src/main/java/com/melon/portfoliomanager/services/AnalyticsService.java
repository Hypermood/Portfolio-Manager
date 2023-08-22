package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.AnalyticsResponse;
import com.melon.portfoliomanager.dtos.responses.AssetStat;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.models.PortfolioItem;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.PortfolioItemRepository;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class AnalyticsService {

    private final PortfolioItemRepository portfolioItemRepository;
    private final UserRepository userRepository;
    private final StockPricesMockApiHttpService stockPricesMockApiHttpService;
    private final CompanyStocksManager companyStocksManager;

    @Autowired
    public AnalyticsService(PortfolioItemRepository portfolioItemRepository,
                            UserRepository userRepository,
                            StockPricesMockApiHttpService stockPricesMockApiHttpService,
                            CompanyStocksManager companyStocksManager) {
        this.portfolioItemRepository = portfolioItemRepository;
        this.userRepository = userRepository;
        this.stockPricesMockApiHttpService = stockPricesMockApiHttpService;
        this.companyStocksManager = companyStocksManager;
    }

    public AnalyticsResponse fetchAnalyticsForUser(String username) {
        User user = validateUserExists(username);
        List<PortfolioItem> portfolioItems = portfolioItemRepository.findByUserId(user.getId());

        List<AssetStat> assetStats = new ArrayList<>();
        double totalPortfolioGainVal = 0.0;
        double totalSpent = 0.0;
        double currentStocksTotalValue = calculateCurrentStocksTotalValue(portfolioItems);

        for (PortfolioItem item : portfolioItems) {
            AssetStat assetStat = calculateAssetStat(item, currentStocksTotalValue);
            assetStats.add(assetStat);

            totalPortfolioGainVal += assetStat.getGainVal();
            totalSpent += item.getTotalBoughtPrice();
        }

        double totalPortfolioGainPct = (totalPortfolioGainVal / totalSpent) * 100;
        if (totalSpent == 0.0) {
            totalPortfolioGainPct = 0.0;
        }

        return buildAnalyticsResponse(username, assetStats, totalPortfolioGainVal, totalPortfolioGainPct, currentStocksTotalValue);
    }

    private User validateUserExists(String username) throws NoSuchUserException {
        return userRepository.findByUsername(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchUserException("There is no such user."));
    }

    private AssetStat calculateAssetStat(PortfolioItem item, Double currentStocksTotalValue) {
        double currentPrice = getCurrentPriceForCompany(item.getCompanyName());
        double currentValue = item.getQuantity() * currentPrice;
        double gainVal = (currentValue + item.getTotalSoldPrice()) - item.getTotalBoughtPrice();
        double gainPct = (((currentValue + item.getTotalSoldPrice()) / item.getTotalBoughtPrice()) - 1) * 100;
        double allocation = currentValue / currentStocksTotalValue;

        return new AssetStat(item.getCompanyName(), gainVal, gainPct, allocation);
    }

    private double getCurrentPriceForCompany(String companyName) {
        Double currentPriceForCompany = companyStocksManager.getCompanyStocks().get(companyName);
        return Objects.requireNonNullElse(currentPriceForCompany, 0.0);
    }

    private double calculateCurrentStocksTotalValue(List<PortfolioItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getQuantity() * getCurrentPriceForCompany(item.getCompanyName()))
                .sum();
    }

    private AnalyticsResponse buildAnalyticsResponse(String username, List<AssetStat> assetStats, double totalPortfolioGainVal, double totalPortfolioGainPct, double currentStocksTotalValue) {
        AnalyticsResponse analyticsResponse = new AnalyticsResponse();
        analyticsResponse.setUsername(username);
        analyticsResponse.setAssets(assetStats);
        analyticsResponse.setTotalPortfolioGainVal(totalPortfolioGainVal);
        analyticsResponse.setTotalPortfolioGainPct(totalPortfolioGainPct);
        analyticsResponse.setTotalPortfolioValue(currentStocksTotalValue);
        return analyticsResponse;
    }
}
