package com.melon.portfoliomanager.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.melon.portfoliomanager.models.PortfolioItem;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.PortfolioItemRepository;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KafkaMessageUtils {
    private final SerializationUtils serializationUtils;
    private final PortfolioItemRepository portfolioItemRepository;
    private final UserRepository userRepository;

    @Autowired
    public KafkaMessageUtils(SerializationUtils serializationUtils, PortfolioItemRepository portfolioItemRepository,
                             UserRepository userRepository) {
        this.serializationUtils = serializationUtils;
        this.portfolioItemRepository = portfolioItemRepository;
        this.userRepository = userRepository;
    }

    public String prepareStockPriceChangesMessage(Map<String, Double> companyStockPrices) throws JsonProcessingException {
        Map<Long, List<String>> userIdsToCompanyNames = mapUserIdsToCompanyNames(getCompanyNames(companyStockPrices));
        Map<Long, String> userIdsToEmails = mapUserIdsToEmails(getUserIds(userIdsToCompanyNames));
        Map<String, Map<String, Double>> userEmailsToCompanyStocks = mapUserEmailsToUserCompanyStockPrices(
                userIdsToEmails,
                userIdsToCompanyNames,
                companyStockPrices
        );

        return serializationUtils.serializeMapToJsonString(userEmailsToCompanyStocks);
    }

    private List<String> getCompanyNames(Map<String, Double> companyStockPrices) {
        return companyStockPrices.keySet().stream().toList();
    }

    private Map<Long, List<String>> mapUserIdsToCompanyNames(List<String> companyNames) {
        List<PortfolioItem> portfolioItems = portfolioItemRepository.findByCompanyNameIn(companyNames);
        Map<Long, List<String>> userIdsToCompanyNames = new HashMap<>();
        portfolioItems.forEach(portfolioItem -> {
            List<String> currentCompanyNames = new ArrayList<>();
            if (userIdsToCompanyNames.containsKey(portfolioItem.getUserId())) {
                currentCompanyNames = userIdsToCompanyNames.get(portfolioItem.getUserId());
            }
            currentCompanyNames.add(portfolioItem.getCompanyName());
            userIdsToCompanyNames.put(portfolioItem.getUserId(), currentCompanyNames);
        });

        return userIdsToCompanyNames;
    }

    private List<Long> getUserIds(Map<Long, List<String>> userIdsToCompanyNames) {
        return userIdsToCompanyNames.keySet().stream().toList();
    }

    private Map<Long, String> mapUserIdsToEmails(List<Long> userIds) {
        List<User> users = userRepository.findByIdIn(userIds);
        return users.stream()
                .collect(Collectors.toMap(User::getId, User::getEmail));
    }

    private Map<String, Map<String, Double>> mapUserEmailsToUserCompanyStockPrices(
            Map<Long, String> userIdsToEmails,
            Map<Long, List<String>> userIdsToCompanyNames,
            Map<String, Double> companyStockPrices
    ) {
        return userIdsToEmails.entrySet().stream()
                .map(userIdToEmail -> {
                    Long userId = userIdToEmail.getKey();
                    String userEmail = userIdToEmail.getValue();
                    List<String> userCompanyNames = userIdsToCompanyNames.get(userId);
                    return Map.entry(userEmail, mapCompanyNamesToCompanyStocks(userCompanyNames, companyStockPrices));
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, Double> mapCompanyNamesToCompanyStocks(List<String> userCompanyNames,
                                                               Map<String, Double> companyStockPrices) {
        return userCompanyNames.stream()
                .map(companyName -> Map.entry(companyName, companyStockPrices.get(companyName)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
