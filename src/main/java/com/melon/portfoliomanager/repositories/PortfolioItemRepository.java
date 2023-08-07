package com.melon.portfoliomanager.repositories;

import com.melon.portfoliomanager.models.PortfolioItem;
import com.melon.portfoliomanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {

    List<PortfolioItem> findByUserAndCompanyName(User user, String companyName);



}
