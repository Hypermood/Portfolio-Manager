package com.melon.portfoliomanager.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public class TransactionDto {

    @NotNull(message = "Invalid user input. The username is null.")
    private String username;

    @NotNull(message = "Invalid user input. The asset symbol is null.")
    private String assetSymbol;

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 1000000000, fraction = 4,message = "Value must contain up to 4 digits in the fraction part and must be a positive number.")
    private Double quantity;

    @NotNull
    @DecimalMin(value = "0.01",message = "The price must be a positive number.")
    private Double price;


    public TransactionDto(String userName, String assetSymbol, Double quantity, Double price) {
        this.username = userName;
        this.assetSymbol = assetSymbol;
        this.quantity = quantity;
        this.price = price;
    }

    public TransactionDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
