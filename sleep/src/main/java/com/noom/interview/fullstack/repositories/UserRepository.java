package com.noom.interview.fullstack.repositories;

import com.noom.interview.fullstack.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}