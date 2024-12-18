package com.ayush.dream_shop.repository;

import com.ayush.dream_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long > {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
