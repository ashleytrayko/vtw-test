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
    public void join(@RequestParam String joinInfo){
        System.out.println(joinInfo);
//        vtwUserService.join(vtwUserDTO);
    }

    @PostMapping("/write")
    public void writeBoard(@ModelAttribute VtwBoard vtwBoard
            , @AuthenticationPrincipal PrincipalDetail principal){
        vtwBoardService.writeBoard(vtwBoard, principal);
    }

    @GetMapping("/delete/{boardNo}")
    public void deleteBoard(@PathVariable long boardNo){
        vtwBoardService.deleteBoard(boardNo);
    }

    @PostMapping ("/update/{boardNo}")
    public void updateBoard(@PathVariable long boardNo
                        , @ModelAttribute VtwBoard vtwBoard){
        vtwBoardService.updateBoard(boardNo, vtwBoard);
    }

    @GetMapping("/resign")
    public void resign(@AuthenticationPrincipal PrincipalDetail principal){
        vtwUserService.resign(principal);
    }

    @PostMapping("/updateUser")
    public void updateUser(@ModelAttribute VtwUserDTO vtwUserDTO){
        vtwUserService.updateUser(vtwUserDTO);
    }

}
