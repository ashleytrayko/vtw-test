package com.example.test.controller;

import com.example.test.configuration.PrincipalDetail;
import com.example.test.domain.VtwBoard;
import com.example.test.domain.VtwUser;
import com.example.test.dto.VtwBoardDTO;
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
    public String join(@RequestBody VtwUserDTO vtwUserDTO){
        return vtwUserService.join(vtwUserDTO);
    }

    @PostMapping("/write")
    public String writeBoard(@RequestBody VtwBoardDTO vtwBoardDTO
            , @AuthenticationPrincipal PrincipalDetail principal){
        return vtwBoardService.writeBoard(vtwBoardDTO, principal);
    }

    @GetMapping("/delete/{boardNo}")
    public String deleteBoard(@PathVariable long boardNo){
        return vtwBoardService.deleteBoard(boardNo);
    }

    @PostMapping ("/update/{boardNo}")
    public String updateBoard(@PathVariable long boardNo
                        ,@RequestBody VtwBoardDTO vtwBoardDTO){
        return vtwBoardService.updateBoard(boardNo, vtwBoardDTO);
    }

    @GetMapping("/resign")
    public String resign(@AuthenticationPrincipal PrincipalDetail principal){
        return vtwUserService.resign(principal);
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestBody VtwUserDTO vtwUserDTO){
        return vtwUserService.updateUser(vtwUserDTO);
    }

}
