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

        User user = validateUser(transactionDto);

        saveTransaction(transactionDto, user);

        List<PortfolioItem> portfolioItemsList = portfolioItemRepository.findByUserIdAndCompanyName(user.getId(), transactionDto.getAssetSymbol());

        PortfolioItem portfolioItem;

        if (portfolioItemsList.isEmpty()) {

            portfolioItem = new PortfolioItem(user.getId(), transactionDto.getAssetSymbol(), transactionDto.getQuantity(), transactionDto.getPrice());

        } else {
            portfolioItem = portfolioItemsList.get(0);
            portfolioItem.setQuantity(portfolioItem.getQuantity() + transactionDto.getQuantity());
            portfolioItem.setQuantity(portfolioItem.getTotalBoughtPrice() + transactionDto.getPrice());

        }
        portfolioItemRepository.save(portfolioItem);

    }

    private void saveTransaction(TransactionDto transactionDto, User user) {

        Transaction transaction = new Transaction(user, TransactionType.BUY, transactionDto.getAssetSymbol(), transactionDto.getQuantity(), transactionDto.getPrice());

        transactionRepository.save(transaction);
    }

    public User validateUser(TransactionDto transactionDto) {
        List<User> userList = userRepository.findByUsername(transactionDto.getUsername());

        if (userList.isEmpty()) {
            throw new NoSuchUserException("There is no user with such username. Enter a valid username.");
        }

        return userList.get(0);
    }


}
