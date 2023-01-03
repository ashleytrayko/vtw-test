package com.example.test.service;

import com.example.test.configuration.PrincipalDetail;
import com.example.test.domain.VtwBoard;
import com.example.test.domain.VtwUser;
import com.example.test.dto.VtwBoardDTO;
import com.example.test.dto.VtwUserDTO;
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

    public String writeBoard(VtwBoardDTO vtwBoardDTO, PrincipalDetail principal){
        VtwBoard vtwBoard = VtwBoard.builder()
                .subject(vtwBoardDTO.getSubject())
                .contents(vtwBoardDTO.getContents())
                .vtwUser(principal.getUser())
                .build();
        VtwBoard returnValue = vtwBoardRepository.saveAndFlush(vtwBoard);
        if(returnValue != null){
            return "success";
        }else {
            return "fail";
        }
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

    @Transactional
    public String updateBoard(long boardNo, VtwBoardDTO vtwBoardDTO) {
        VtwBoard targetBoard = vtwBoardRepository.findById(boardNo)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 찾기 실패");
                });
        targetBoard.setSubject(vtwBoardDTO.getSubject());
        targetBoard.setContents(vtwBoardDTO.getContents());
        VtwBoard vtwBoard = vtwBoardRepository.saveAndFlush(targetBoard);
        if (vtwBoard != null){
            return "success";
        } else {
            return "fail";
        }
    }

    @Transactional
    public String deleteBoard(long boardNo) {
//        vtwBoardRepository.deleteById(boardNo);
        int returnValue = vtwBoardRepository.customDeleteById(boardNo);
        if (returnValue >= 1){
            return "success";
        }else{
            return "fail";
        }
    }
}
