package com.example.test.service;

import com.example.test.configuration.PrincipalDetail;
import com.example.test.domain.VtwBoard;
import com.example.test.domain.VtwUser;
import com.example.test.repository.VtwBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class VtwBoardService {
    private final VtwBoardRepository vtwBoardRepository;

    public void writeBoard(VtwBoard vtwBoard, PrincipalDetail principal){
        vtwBoard.setVtwUser((VtwUser) principal.getUser());
        vtwBoardRepository.save(vtwBoard);
    }
    @Transactional(readOnly = true)
    public Page<VtwBoard> boardList(Pageable pageable) {
        return vtwBoardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public VtwBoard selectOne(long boardNo){
        return vtwBoardRepository.findById(boardNo).orElseThrow(()->{
            return new IllegalArgumentException("상세보기 실패");
        });
    }
}
