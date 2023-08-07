package com.melon.portfoliomanager.services;


import com.melon.portfoliomanager.dtos.TransactionDto;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.models.PortfolioItem;
import com.melon.portfoliomanager.models.Transaction;
import com.melon.portfoliomanager.models.TransactionType;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.PortfolioItemRepository;
import com.melon.portfoliomanager.repositories.TransactionRepository;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    private TransactionRepository transactionRepository;
    private PortfolioItemRepository portfolioItemRepository;
    private UserRepository userRepository;


    @Autowired
    public AssetService(TransactionRepository transactionRepository, PortfolioItemRepository portfolioItemRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.portfolioItemRepository = portfolioItemRepository;
        this.userRepository = userRepository;
    }

    public void buyStock(TransactionDto transactionDto) {

        List<User> userList = userRepository.findByUsername(transactionDto.getUsername());

        if (userList.isEmpty()) {
            throw new NoSuchUserException("There is no user with such username. Enter a valid username.");
        }

        User user = userList.get(0);
        Transaction transaction = new Transaction(user, TransactionType.BUY, transactionDto.getAssetSymbol(), transactionDto.getQuantity(), transactionDto.getPrice());

        transactionRepository.save(transaction);

        List<PortfolioItem> pfItemList = portfolioItemRepository.findByUserAndCompanyName(user, transactionDto.getAssetSymbol());

        if (pfItemList.isEmpty()) {
            PortfolioItem pfItem = new PortfolioItem(user, transactionDto.getAssetSymbol(), transactionDto.getQuantity(), transactionDto.getPrice());
            portfolioItemRepository.save(pfItem);
        } else {
            PortfolioItem previousItem = pfItemList.get(0);
            PortfolioItem pfItem = new PortfolioItem(user, transactionDto.getAssetSymbol(), transactionDto.getQuantity() + previousItem.getQuantity(), transactionDto.getPrice() + previousItem.getTotalBoughtPrice());
            portfolioItemRepository.delete(previousItem);
            portfolioItemRepository.save(pfItem);
        }


    }
}
