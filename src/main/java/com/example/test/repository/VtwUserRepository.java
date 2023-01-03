package com.example.test.repository;

import com.example.test.domain.VtwUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VtwUserRepository extends JpaRepository<VtwUser, Long>{
    Optional<VtwUser> findByUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM VtwUser WHERE userId = ?1")
    int customDeleteById(Long userId);

}
