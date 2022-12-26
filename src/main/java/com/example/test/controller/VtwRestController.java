package com.example.test.controller;

import com.example.test.configuration.PrincipalDetail;
import com.example.test.domain.VtwBoard;
import com.example.test.domain.VtwUser;
import com.example.test.dto.VtwUserDTO;
import com.example.test.service.VtwBoardService;
import com.example.test.service.VtwUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

// 데이터를 처리하는 RestController 여기서는 @RequiredArgContructor 어노테이션 사용해봄
@RequiredArgsConstructor
@RestController
public class VtwRestController {

    private final VtwUserService vtwUserService;
    private final VtwBoardService vtwBoardService;
    
    @PostMapping("/join")
    public void join(@ModelAttribute VtwUserDTO vtwUserDTO
                    ,HttpServletResponse response){
        try {
            vtwUserService.join(vtwUserDTO);
            response.sendRedirect("/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/write")
    public void writeBoard(@ModelAttribute VtwBoard vtwBoard
            , @AuthenticationPrincipal PrincipalDetail principal
            ,HttpServletResponse response){
        vtwBoardService.writeBoard(vtwBoard, principal);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/delete/{boardNo}")
    public void deleteBoard(@PathVariable long boardNo
                            ,HttpServletResponse response){

        vtwBoardService.deleteBoard(boardNo);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping ("/update/{boardNo}")
    public void updateBoard(@PathVariable long boardNo
                        , @ModelAttribute VtwBoard vtwBoard
                        , HttpServletResponse response){
        vtwBoardService.updateBoard(boardNo, vtwBoard);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/resign")
    public void resign(@AuthenticationPrincipal PrincipalDetail principal
                        , HttpServletResponse response){
        vtwUserService.resign(principal);
        try {
            response.sendRedirect("/logout");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/updateUser")
    public void updateUser(@ModelAttribute VtwUserDTO vtwUserDTO
                            , HttpServletResponse response){
        vtwUserService.updateUser(vtwUserDTO);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
