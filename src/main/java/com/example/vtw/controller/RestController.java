package com.example.vtw.controller;

import com.example.vtw.configuration.PrincipalDetail;
import com.example.vtw.dto.BoardDTO;
import com.example.vtw.dto.UserDTO;
import com.example.vtw.service.BoardService;
import com.example.vtw.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


// 데이터를 처리하는 RestController 여기서는 @RequiredArgContructor 어노테이션 사용해봄
@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final UserService userService;
    private final BoardService boardService;
    
    @PostMapping("/join")
    public String join(@RequestBody UserDTO userDTO){
        return userService.join(userDTO);
    }

    @PostMapping("/write")
    public String writeBoard(@RequestBody BoardDTO boardDTO
            , @AuthenticationPrincipal PrincipalDetail principal){
        return boardService.writeBoard(boardDTO, principal);
    }

    @GetMapping("/delete/{boardNo}")
    public String deleteBoard(@PathVariable long boardNo){
        return boardService.deleteBoard(boardNo);
    }

    @PostMapping ("/update/{boardNo}")
    public String updateBoard(@PathVariable long boardNo
                        ,@RequestBody BoardDTO boardDTO){
        return boardService.updateBoard(boardNo, boardDTO);
    }

    @GetMapping("/resign")
    public String resign(@AuthenticationPrincipal PrincipalDetail principal){
        return userService.resign(principal);
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestBody UserDTO userDTO){
        return userService.updateUser(userDTO);
    }

}
