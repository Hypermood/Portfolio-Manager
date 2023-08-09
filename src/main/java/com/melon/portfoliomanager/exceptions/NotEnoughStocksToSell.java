package com.melon.portfoliomanager.exceptions;

public class NotEnoughStocksToSell extends RuntimeException{
    public NotEnoughStocksToSell() {
        super("The user does not have enough stocks to sell such amount as per the request.");
    }
}
