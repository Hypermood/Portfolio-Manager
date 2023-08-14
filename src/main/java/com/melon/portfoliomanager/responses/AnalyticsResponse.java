package com.melon.portfoliomanager.responses;

import java.util.List;

public class AnalyticsResponse {

    private String username;
    private Double totalPortfolioValue;
    private Double totalPortfolioGainVal;
    private Double totalPortfolioGainPct;
    private List<AssetStat> assets;


    public AnalyticsResponse(String userName, Double totalPortfolioValue, Double totalPortfolioGainVal, Double totalPortfolioGainPct, List<AssetStat> assets) {
        this.username = userName;
        this.totalPortfolioValue = totalPortfolioValue;
        this.totalPortfolioGainVal = totalPortfolioGainVal;
        this.totalPortfolioGainPct = totalPortfolioGainPct;
        this.assets = assets;
    }

    public AnalyticsResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getTotalPortfolioValue() {
        return totalPortfolioValue;
    }

    public void setTotalPortfolioValue(Double totalPortfolioValue) {
        this.totalPortfolioValue = totalPortfolioValue;
    }

    public Double getTotalPortfolioGainVal() {
        return totalPortfolioGainVal;
    }

    public void setTotalPortfolioGainVal(Double totalPortfolioGainVal) {
        this.totalPortfolioGainVal = totalPortfolioGainVal;
    }

    public Double getTotalPortfolioGainPct() {
        return totalPortfolioGainPct;
    }

    public void setTotalPortfolioGainPct(Double totalPortfolioGainPct) {
        this.totalPortfolioGainPct = totalPortfolioGainPct;
    }

    public List<AssetStat> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetStat> assets) {
        this.assets = assets;
    }
}
