package com.example.test.controller;

import com.example.test.domain.TestUser;
import com.example.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/")
    public String mainPage(){
        return "mainPage";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "loginPage";
    }

    @GetMapping("/join")
    public String joinPage(){
        return "joinPage";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute TestUser user){
        String result = testService.join(user);
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
}
