package com.melon.portfoliomanager.repositories;

import com.melon.portfoliomanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);

    @Transactional
    long deleteByUsername(String username);

    List<User> findByIdIn(List<Long> ids);
}
