package com.example.test.repository;

import com.example.test.domain.VtwBoard;
import com.example.test.domain.VtwUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VtwBoardRepository extends JpaRepository<VtwBoard, Long>{

}
