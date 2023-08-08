package com.melon.portfoliomanager.models;

import jakarta.persistence.*;


@Entity
@Table(name = "portfolio_items")
public class PortfolioItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(name = "company_name")
    private String companyName;

    private Double quantity;

    @Column(name = "total_bought_price")
    private Double totalBoughtPrice;

    public PortfolioItem(Long userId, String companyName, Double quantity, Double totalBoughtPrice) {
        this.userId = userId;
        this.companyName = companyName;
        this.quantity = quantity;
        this.totalBoughtPrice = totalBoughtPrice;
    }

    public PortfolioItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getTotalBoughtPrice() {
        return totalBoughtPrice;
    }

    public void setTotalBoughtPrice(Double totalBoughtPrice) {
        this.totalBoughtPrice = totalBoughtPrice;
    }
}
