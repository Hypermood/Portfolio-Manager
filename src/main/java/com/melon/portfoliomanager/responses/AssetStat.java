package com.melon.portfoliomanager.responses;

import java.text.DecimalFormat;

public class AssetStat {

    private String company;
    private Double gainVal;
    private Double gainPct;
    private Double allocation;


    public AssetStat(String company, Double gainVal, Double gainPct, Double allocation) {
        this.company = company;
        this.gainVal = Double.valueOf(new DecimalFormat("#.##").format(gainVal));
        this.gainPct = Double.valueOf(new DecimalFormat("#.##").format(gainPct));
        this.allocation = Double.valueOf(new DecimalFormat("#.##").format(allocation));
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
        this.gainVal = Double.valueOf(new DecimalFormat("#.##").format(gainVal));
    }

    public Double getGainPct() {
        return gainPct;
    }

    public void setGainPct(Double gainPct) {
        this.gainPct = Double.valueOf(new DecimalFormat("#.##").format(gainPct));
    }

    public Double getAllocation() {
        return allocation;
    }

    public void setAllocation(Double allocation) {
        this.allocation = Double.valueOf(new DecimalFormat("#.##").format(allocation));
    }
}
