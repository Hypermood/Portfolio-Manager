package com.melon.portfoliomanager.responses;

import java.text.DecimalFormat;
import java.util.List;

public class AnalyticsResponse {

    private String username;
    private Double totalPortfolioValue;
    private Double totalPortfolioGainVal;
    private Double totalPortfolioGainPct;
    private List<AssetStat> assets;


    public AnalyticsResponse(String userName, Double totalPortfolioValue, Double totalPortfolioGainVal, Double totalPortfolioGainPct, List<AssetStat> assets) {
        this.username = userName;
        this.totalPortfolioValue = Double.valueOf(new DecimalFormat("#.##").format(totalPortfolioValue));
        this.totalPortfolioGainVal = Double.valueOf(new DecimalFormat("#.##").format(totalPortfolioGainVal));
        this.totalPortfolioGainPct = Double.valueOf(new DecimalFormat("#.##").format(totalPortfolioGainPct));
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
        this.totalPortfolioValue = Double.valueOf(new DecimalFormat("#.##").format(totalPortfolioValue));
    }

    public Double getTotalPortfolioGainVal() {
        return totalPortfolioGainVal;
    }

    public void setTotalPortfolioGainVal(Double totalPortfolioGainVal) {
        this.totalPortfolioGainVal = Double.valueOf(new DecimalFormat("#.##").format(totalPortfolioGainVal));
    }

    public Double getTotalPortfolioGainPct() {
        return totalPortfolioGainPct;
    }

    public void setTotalPortfolioGainPct(Double totalPortfolioGainPct) {

        this.totalPortfolioGainPct = Double.valueOf(new DecimalFormat("#.##").format(totalPortfolioGainPct));
    }

    public List<AssetStat> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetStat> assets) {
        this.assets = assets;
    }
}
