package com.example.vtw.repository;

import com.example.vtw.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM User WHERE userId = ?1")
    int customDeleteById(Long userId);

}
