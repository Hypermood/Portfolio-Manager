package com.melon.portfoliomanager.repositories;

import com.melon.portfoliomanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
