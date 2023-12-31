package com.melon.portfoliomanager.services;


import com.melon.portfoliomanager.dtos.TransactionDto;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.exceptions.NotEnoughStocksToSell;
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
import java.util.Objects;

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

        saveTransaction(transactionDto, user, TransactionType.BUY);

        List<PortfolioItem> portfolioItemsList = portfolioItemRepository.findByUserIdAndCompanyName(user.getId(), transactionDto.getAssetSymbol());

        PortfolioItem portfolioItem;

        if (portfolioItemsList.isEmpty()) {

            portfolioItem = new PortfolioItem(user.getId(), transactionDto.getAssetSymbol(), transactionDto.getQuantity(), transactionDto.getPrice(), 0.0);

        } else {
            portfolioItem = portfolioItemsList.get(0);
            portfolioItem.setQuantity(portfolioItem.getQuantity() + transactionDto.getQuantity());
            portfolioItem.setTotalBoughtPrice(portfolioItem.getTotalBoughtPrice() + transactionDto.getPrice());

        }
        portfolioItemRepository.save(portfolioItem);

    }

    private void saveTransaction(TransactionDto transactionDto, User user, TransactionType type) {

        Transaction transaction = new Transaction(user.getId(), type, transactionDto.getAssetSymbol(), transactionDto.getQuantity(), transactionDto.getPrice());

        transactionRepository.save(transaction);
    }

    public User validateUser(TransactionDto transactionDto) {
        List<User> userList = userRepository.findByUsername(transactionDto.getUsername());

        if (userList.isEmpty()) {
            throw new NoSuchUserException("There is no user with such username. Enter a valid username.");
        }

        return userList.get(0);
    }

    public void sellStock(TransactionDto transactionDto) {

        User user = validateUser(transactionDto);

        List<PortfolioItem> portfolioItemList = portfolioItemRepository.findByUserIdAndCompanyName(user.getId(), transactionDto.getAssetSymbol());

        if (portfolioItemList.isEmpty()) {
            throw new NotEnoughStocksToSell();
        }

        PortfolioItem portfolioItem;
        portfolioItem = portfolioItemList.get(0);

        if (portfolioItem.getQuantity() < transactionDto.getQuantity()) {
            throw new NotEnoughStocksToSell();
        }

        portfolioItem.setQuantity(portfolioItem.getQuantity() - transactionDto.getQuantity());
        portfolioItem.setTotalSoldPrice(portfolioItem.getTotalSoldPrice() + transactionDto.getPrice());

        saveTransaction(transactionDto, user, TransactionType.SELL);

    }

}
