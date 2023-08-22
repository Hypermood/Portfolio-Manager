package com.melon.portfoliomanager.dtos.responses;

import java.text.DecimalFormat;

public class AssetStatDto {

    private String company;
    private Double gainVal;
    private Double gainPct;
    private Double allocation;


    public AssetStatDto() {
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

        if (gainVal == null) {
            throw new RuntimeException("GainVal cannot be null");
        }

        this.gainVal = Double.valueOf(new DecimalFormat("#.##").format(gainVal));
    }

    public Double getGainPct() {
        return gainPct;
    }

    public void setGainPct(Double gainPct) {

        if (gainPct == null) {
            throw new RuntimeException("GainPct cannot be null");
        }

        this.gainPct = Double.valueOf(new DecimalFormat("#.##").format(gainPct));
    }

    public Double getAllocation() {
        return allocation;
    }

    public void setAllocation(Double allocation) {

        if (allocation == null) {
            throw new RuntimeException("Allocation cannot be null");
        }

        this.allocation = Double.valueOf(new DecimalFormat("#.##").format(allocation));
    }
}
