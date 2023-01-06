package com.example.vtw.service;

import com.example.vtw.configuration.PrincipalDetail;
import com.example.vtw.domain.Board;
import com.example.vtw.dto.BoardDTO;
import com.example.vtw.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public String writeBoard(BoardDTO boardDTO, PrincipalDetail principal){
        Board board = Board.builder()
                .subject(boardDTO.getSubject())
                .contents(boardDTO.getContents())
                .user(principal.getUser())
                .build();
        Board returnValue = boardRepository.saveAndFlush(board);
        if(returnValue != null){
            return "success";
        }else {
            return "fail";
        }
    }
    @Transactional(readOnly = true)
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board selectOne(long boardNo){
        return boardRepository.findById(boardNo).orElseThrow(()->{
            return new IllegalArgumentException("상세보기 실패");
        });
    }

    @Transactional
    public String updateBoard(long boardNo, BoardDTO boardDTO) {
        Board targetBoard = boardRepository.findById(boardNo)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 찾기 실패");
                });
        targetBoard.setSubject(boardDTO.getSubject());
        targetBoard.setContents(boardDTO.getContents());
        Board board = boardRepository.saveAndFlush(targetBoard);
        if (board != null){
            return "success";
        } else {
            return "fail";
        }
    }

    @Transactional
    public String deleteBoard(long boardNo) {
//        vtwBoardRepository.deleteById(boardNo);
        int returnValue = boardRepository.customDeleteById(boardNo);
        if (returnValue >= 1){
            return "success";
        }else{
            return "fail";
        }
    }
}
