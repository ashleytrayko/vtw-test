package com.example.vtw.repository;

import com.example.vtw.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

    @Modifying
    @Query(value = "DELETE FROM Board WHERE boardNo = ?1")
    int customDeleteById(Long boardNo);
}
