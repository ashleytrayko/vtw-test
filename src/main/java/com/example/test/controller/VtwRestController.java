package com.example.test.controller;

import com.example.test.configuration.PrincipalDetail;
import com.example.test.domain.VtwBoard;
import com.example.test.domain.VtwUser;
import com.example.test.service.VtwBoardService;
import com.example.test.service.VtwUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

// 데이터를 처리하는 RestController 여기서는 @RequiredArgContructor 어노테이션 사용해봄
@RequiredArgsConstructor
@RestController
public class VtwRestController {

    private final VtwUserService vtwUserService;
    private final VtwBoardService vtwBoardService;
    
    @PostMapping("/join")
    public String join(@ModelAttribute VtwUser user){
        String result = vtwUserService.join(user);
        if(result.equals("ok")){
            return "mainPage";
        }else return "errorPage";
    }

    @GetMapping("/user")
    public String userPage(Principal principal, Model model){
        if(principal != null){
            model.addAttribute("userInfo", principal);
        }
        return "/member/userInfo";
    }

    @PostMapping("/write")
    public void writeBoard(@ModelAttribute VtwBoard vtwBoard, @AuthenticationPrincipal PrincipalDetail principal){
        vtwBoardService.writeBoard(vtwBoard, principal);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBoard(@PathVariable long boardNo){
        vtwBoardService.deleteBoard(boardNo);
    }

    @PutMapping("/update/{id}")
    public void updateBoard(@PathVariable long boardNo, @ModelAttribute VtwBoard vtwBoard){
        vtwBoardService.updateBoard(boardNo, vtwBoard);
    }

}
