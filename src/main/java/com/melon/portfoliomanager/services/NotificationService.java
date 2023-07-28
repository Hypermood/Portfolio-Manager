package com.melon.portfoliomanager.services;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationService {
    public void notifyUser(Map<String, Double> companyStocks) {
        // TODO: Push notification to the message broker here.
    }
}
