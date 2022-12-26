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

import java.security.Principal;

// 데이터를 처리하는 RestController 여기서는 @RequiredArgContructor 어노테이션 사용해봄
@RequiredArgsConstructor
@RestController
public class VtwRestController {

    private final VtwUserService vtwUserService;
    private final VtwBoardService vtwBoardService;
    
    @PostMapping("/join")
    public ModelAndView join(@ModelAttribute VtwUserDTO vtwUserDTO
                            ,ModelAndView mv){
        vtwUserService.join(vtwUserDTO);
        mv.setViewName("redirect:/");
        return mv;
    }

    @GetMapping("/mypage")
    public ModelAndView userPage(PrincipalDetail principal, ModelAndView mv){
        if(principal != null){
            mv.addObject("userInfo", principal.getUser());
            mv.setViewName("member/myPage");
        }
        return mv;
    }

    @PostMapping("/write")
    public ModelAndView writeBoard(@ModelAttribute VtwBoard vtwBoard
            , @AuthenticationPrincipal PrincipalDetail principal
            , ModelAndView mv){
        vtwBoardService.writeBoard(vtwBoard, principal);
        mv.setViewName("redirect:/");
        return mv;
    }

    @GetMapping("/delete/{boardNo}")
    public ModelAndView deleteBoard(@PathVariable long boardNo
                                , ModelAndView mv){

        vtwBoardService.deleteBoard(boardNo);
        mv.setViewName("redirect:/");
        return mv;
    }

    @PostMapping ("/update/{boardNo}")
    public ModelAndView updateBoard(@PathVariable long boardNo
                        , @ModelAttribute VtwBoard vtwBoard
                        , ModelAndView mv){
        vtwBoardService.updateBoard(boardNo, vtwBoard);
        mv.setViewName("redirect:/");
        return mv;
    }

    @GetMapping("/resign")
    public ModelAndView resign(ModelAndView mv, @AuthenticationPrincipal PrincipalDetail principal){
        vtwUserService.resign(principal);
        mv.setViewName("redirect:/logout");
        return mv;
    }

    @PostMapping("/updateUser")
    public ModelAndView updateUser(ModelAndView mv
                                    ,@ModelAttribute VtwUserDTO vtwUserDTO){
        vtwUserService.updateUser(vtwUserDTO);
        mv.setViewName("redirect:/");
        return mv;
    }

}
