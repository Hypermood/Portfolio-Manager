package com.melon.portfoliomanager.dtos.responses;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsResponseDto {

    private String username;
    private Double totalPortfolioValue;
    private Double totalPortfolioGainVal;
    private Double totalPortfolioGainPct;
    private List<AssetStatDto> assets;


    public AnalyticsResponseDto() {
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

        if (totalPortfolioValue == null) {
            throw new RuntimeException("TotalPortfolioValue cannot be null");
        }

        this.totalPortfolioValue = Double.valueOf(new DecimalFormat("#.##").format(totalPortfolioValue));
    }

    public Double getTotalPortfolioGainVal() {
        return totalPortfolioGainVal;
    }

    public void setTotalPortfolioGainVal(Double totalPortfolioGainVal) {

        if(totalPortfolioGainVal == null){
            throw new RuntimeException("TotalPortfolioGainVal cannot be null");
        }

        this.totalPortfolioGainVal = Double.valueOf(new DecimalFormat("#.##").format(totalPortfolioGainVal));
    }

    public Double getTotalPortfolioGainPct() {
        return totalPortfolioGainPct;
    }

    public void setTotalPortfolioGainPct(Double totalPortfolioGainPct) {

        if(totalPortfolioGainPct == null){
            throw new RuntimeException("TotalPortfolioGainPct cannot be null");
        }

        this.totalPortfolioGainPct = Double.valueOf(new DecimalFormat("#.##").format(totalPortfolioGainPct));
    }

    public List<AssetStatDto> getAssets() {

        if(this.assets == null){
            return null;
        }

        return new ArrayList<>(this.assets);
    }

    public void setAssets(List<AssetStatDto> assets) {

        if(assets == null){
            throw new RuntimeException("Assets' list cannot be null");
        }

        this.assets = new ArrayList<>(assets);
    }
}
