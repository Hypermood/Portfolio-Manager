package com.melon.portfoliomanager.responses;

public class AssetStat {

    private String company;
    private Double gainVal;
    private Double gainPct;
    private Double allocation;


    public AssetStat(String company, Double gainVal, Double gainPct, Double allocation) {
        this.company = company;
        this.gainVal = gainVal;
        this.gainPct = gainPct;
        this.allocation = allocation;
    }


    public AssetStat() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getGainVal() {
        return gainVal;
    }

    public void setGainVal(Double gainVal) {
        this.gainVal = gainVal;
    }

    public Double getGainPct() {
        return gainPct;
    }

    public void setGainPct(Double gainPct) {
        this.gainPct = gainPct;
    }

    public Double getAllocation() {
        return allocation;
    }

    public void setAllocation(Double allocation) {
        this.allocation = allocation;
    }
}
