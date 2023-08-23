package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.AnalyticsResponseDto;
import com.melon.portfoliomanager.dtos.responses.AssetStatDto;
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
    private final CompanyStocksManager companyStocksManager;

    @Autowired
    public AnalyticsService(PortfolioItemRepository portfolioItemRepository,
                            UserRepository userRepository,
                            CompanyStocksManager companyStocksManager) {
        this.portfolioItemRepository = portfolioItemRepository;
        this.userRepository = userRepository;
        this.companyStocksManager = companyStocksManager;
    }

    public AnalyticsResponseDto fetchAnalyticsForUser(String username) {
        User user = validateUserExists(username);
        List<PortfolioItem> portfolioItems = portfolioItemRepository.findByUserId(user.getId());

        List<AssetStatDto> assetStats = new ArrayList<>();
        double totalPortfolioGainVal = 0.0;
        double totalSpent = 0.0;
        double totalPortfolioGainPct = 0.0;
        double currentStocksTotalValue = calculateCurrentStocksTotalValue(portfolioItems);

        for (PortfolioItem item : portfolioItems) {
            AssetStatDto assetStat = calculateAssetStat(item, currentStocksTotalValue);
            assetStats.add(assetStat);

            totalPortfolioGainVal += assetStat.getGainVal();
            totalSpent += item.getTotalBoughtPrice();
        }

        if (Double.compare(totalSpent, 0.0) != 0) {
            totalPortfolioGainPct = (totalPortfolioGainVal / totalSpent) * 100;
        }

        return buildAnalyticsResponse(username, assetStats, totalPortfolioGainVal, totalPortfolioGainPct, currentStocksTotalValue);
    }

    private User validateUserExists(String username) throws NoSuchUserException {
        return userRepository.findByUsername(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchUserException("There is no such user."));
    }

    private AssetStatDto calculateAssetStat(PortfolioItem item, Double currentStocksTotalValue) {
        double currentPrice = getCurrentPriceForCompany(item.getCompanyName());
        double currentCompanyStocksActualPrice = item.getQuantity() * currentPrice;
        double gainVal = (currentCompanyStocksActualPrice + item.getTotalSoldPrice()) - item.getTotalBoughtPrice();
        double gainPct = (((currentCompanyStocksActualPrice + item.getTotalSoldPrice()) / item.getTotalBoughtPrice()) - 1) * 100;
        double allocation = currentCompanyStocksActualPrice / currentStocksTotalValue;

        AssetStatDto assetStatDto = new AssetStatDto();
        assetStatDto.setCompany(item.getCompanyName());
        assetStatDto.setGainVal(gainVal);
        assetStatDto.setGainPct(gainPct);
        assetStatDto.setAllocation(allocation);

        return assetStatDto;
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

    private AnalyticsResponseDto buildAnalyticsResponse(String username, List<AssetStatDto> assetStats, double totalPortfolioGainVal, double totalPortfolioGainPct, double currentStocksTotalValue) {
        AnalyticsResponseDto analyticsResponseDto = new AnalyticsResponseDto();
        analyticsResponseDto.setUsername(username);
        analyticsResponseDto.setAssets(assetStats);
        analyticsResponseDto.setTotalPortfolioGainVal(totalPortfolioGainVal);
        analyticsResponseDto.setTotalPortfolioGainPct(totalPortfolioGainPct);
        analyticsResponseDto.setTotalPortfolioValue(currentStocksTotalValue);
        return analyticsResponseDto;
    }
}
