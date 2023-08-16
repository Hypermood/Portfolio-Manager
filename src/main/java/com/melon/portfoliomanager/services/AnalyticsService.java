package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.responses.StockPricesDTO;
import com.melon.portfoliomanager.exceptions.AnalyticsException;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.models.PortfolioItem;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.PortfolioItemRepository;
import com.melon.portfoliomanager.repositories.TransactionRepository;
import com.melon.portfoliomanager.repositories.UserRepository;
import com.melon.portfoliomanager.dtos.responses.AnalyticsResponse;
import com.melon.portfoliomanager.dtos.responses.AssetStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    //To be removed if not used
    private TransactionRepository transactionRepository;
    private PortfolioItemRepository portfolioItemRepository;
    private UserRepository userRepository;
    private StockPricesMockApiHttpService stockPricesMockApiHttpService;


    @Autowired
    public AnalyticsService(TransactionRepository transactionRepository, PortfolioItemRepository portfolioItemRepository,
                            UserRepository userRepository, StockPricesMockApiHttpService stockPricesMockApiHttpService) {
        this.transactionRepository = transactionRepository;
        this.portfolioItemRepository = portfolioItemRepository;
        this.userRepository = userRepository;
        this.stockPricesMockApiHttpService = stockPricesMockApiHttpService;
    }


    public AnalyticsResponse fetchAnalyticsForUser(String username) throws Exception {

        List<User> userOp = userRepository.findByUsername(username);

        if (userOp.isEmpty()) {
            throw new NoSuchUserException("There is no such user.");
        }

        User user = userOp.get(0);
        List<PortfolioItem> portfolioItems = portfolioItemRepository.findByUserId(user.getId());

        double currentStocksTotalValue = calculateCurrentStocksTotalValue(portfolioItems);
        double totalPortfolioGainVal = 0.0;
        double totalSpent = 0.0;
        List<AssetStat> assets = new ArrayList<>();

        for (PortfolioItem item : portfolioItems) {
            double currentPrice = getCurrentPriceForCompany(item.getCompanyName());
            double currentValue = item.getQuantity() * currentPrice;

            AssetStat assetStat = new AssetStat();
            assetStat.setCompany(item.getCompanyName());

            assetStat.setGainVal((currentValue + item.getTotalSoldPrice()) - item.getTotalBoughtPrice());
            assetStat.setGainPct((((currentValue + item.getTotalSoldPrice()) / item.getTotalBoughtPrice()) - 1) * 100);
            assetStat.setAllocation(currentValue / currentStocksTotalValue);
            assets.add(assetStat);

            totalPortfolioGainVal += assetStat.getGainVal();
            totalSpent += item.getTotalBoughtPrice();
        }

        double totalPortfolioGainPct = (totalPortfolioGainVal / totalSpent) * 100;

        AnalyticsResponse analyticsDto = new AnalyticsResponse();
        analyticsDto.setUsername(username);
        analyticsDto.setTotalPortfolioValue(currentStocksTotalValue);
        analyticsDto.setTotalPortfolioGainVal(totalPortfolioGainVal);
        analyticsDto.setTotalPortfolioGainPct(totalPortfolioGainPct);
        analyticsDto.setAssets(assets);

        return analyticsDto;
    }

    private double getCurrentPriceForCompany(String companyName) {

        StockPricesDTO body = stockPricesMockApiHttpService.getStockPrices().getBody();

        Map<String, Double> stockPrices = body.getStockPrices();

        if (stockPrices.containsKey(companyName)) {
            return body.getStockPrices().get(companyName);
        } else {
            throw new AnalyticsException("There is no such company.");
        }
    }

    private double calculateCurrentStocksTotalValue(List<PortfolioItem> items) {
        return items.stream().mapToDouble(item->item.getQuantity() * getCurrentPriceForCompany(item.getCompanyName())).sum();
    }

}
