package com.example.test.repository;

import com.example.test.domain.TestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<TestUser, Long>{
    Optional<TestUser> findByUsername(String username);

}
