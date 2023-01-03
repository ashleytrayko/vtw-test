package com.example.test.repository;

import com.example.test.domain.VtwBoard;
import com.example.test.domain.VtwUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VtwBoardRepository extends JpaRepository<VtwBoard, Long>{

    @Modifying
    @Query(value = "DELETE FROM VtwBoard WHERE boardNo = ?1")
    int customDeleteById(Long boardNo);
}
