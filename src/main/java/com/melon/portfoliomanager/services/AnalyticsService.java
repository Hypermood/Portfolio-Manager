package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.models.PortfolioItem;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.PortfolioItemRepository;
import com.melon.portfoliomanager.repositories.TransactionRepository;
import com.melon.portfoliomanager.repositories.UserRepository;
import com.melon.portfoliomanager.responses.AnalyticsResponse;
import com.melon.portfoliomanager.responses.AssetStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {

    private TransactionRepository transactionRepository;
    private PortfolioItemRepository portfolioItemRepository;
    private UserRepository userRepository;


    @Autowired
    public AnalyticsService(TransactionRepository transactionRepository, PortfolioItemRepository portfolioItemRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.portfolioItemRepository = portfolioItemRepository;
        this.userRepository = userRepository;
    }

    public AnalyticsResponse fetchAnalyticsForUser(String username) {

        List<User> userOp = userRepository.findByUsername(username);

        if(userOp.isEmpty()){
            throw new NoSuchUserException("There is no such user.");
        }

        User user = userOp.get(0);
        List<PortfolioItem> portfolioItems = portfolioItemRepository.findByUserId(user.getId());

        double totalPortfolioValue = 0.0;
        double totalSpent = 0.0;
        List<AssetStat> assets = new ArrayList<>();

        for (PortfolioItem item : portfolioItems) {
            double currentPrice = getCurrentPriceForCompany(item.getCompanyName());
            double currentValue = item.getQuantity() * currentPrice;

            AssetStat assetStat = new AssetStat();
            assetStat.setCompany(item.getCompanyName());

            assetStat.setGainVal(currentValue - item.getTotalBoughtPrice());

            assetStat.setGainPct( ((currentValue / item.getTotalBoughtPrice()) - 1) * 100 );

            assets.add(assetStat);

            totalPortfolioValue += currentValue;
            totalSpent += item.getTotalBoughtPrice();
        }

        for (AssetStat asset : assets) {
            asset.setAllocation((asset.getGainVal() / totalPortfolioValue) * 100);
        }

        double totalPortfolioGainVal = totalPortfolioValue - totalSpent;
        double totalPortfolioGainPct = ((totalPortfolioValue / totalSpent) - 1) * 100;

        AnalyticsResponse analyticsDto = new AnalyticsResponse();
        analyticsDto.setUserName(username);
        analyticsDto.setTotalPortfolioValue(totalPortfolioValue);
        analyticsDto.setTotalPortfolioGainVal(totalPortfolioGainVal);
        analyticsDto.setTotalPortfolioGainPct(totalPortfolioGainPct);
        analyticsDto.setAssets(assets);

        return analyticsDto;
    }

    private double getCurrentPriceForCompany(String companyName) {
        //To be integrated with external API
        return 200.0;
    }
}
