package com.example.test.controller;

import com.example.test.configuration.PrincipalDetail;
import com.example.test.service.VtwBoardService;
import com.example.test.service.VtwUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// 단순 View만 처리하는 Controller로 수정
@Controller
public class VtwController {
    

//    필드 의존성 주입으로 순환참조 문제 발생가능 -> 실제로 발생함. 생성자 의존성 주입으로 수정
//    @Autowired
//    private VtwService vtwService;

    private final VtwUserService vtwUserService;
    private final VtwBoardService vtwBoardService;

    public VtwController(VtwUserService vtwUserService, VtwBoardService vtwBoardService){
        this.vtwUserService = vtwUserService;
        this.vtwBoardService = vtwBoardService;
    }

    @GetMapping("/")
    public String mainPage(
            Model model,
            @PageableDefault(size = 10, sort = "boardNo", direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("boards", vtwBoardService.boardList(pageable));
        return "mainPage";
    }

    @GetMapping("/error")
    public String errorPage(){
        return "errorPage";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "loginPage";
    }

    @GetMapping("/join")
    public String joinPage(){
        return "joinPage";
    }

    @GetMapping("/write")
    public String writeForm(){
        return "/board/writeForm";
    }

    @GetMapping("/board/updateForm")
    public String updateForm(@RequestParam long boardNo, Model model) {
        model.addAttribute("board", vtwBoardService.selectOne(boardNo));
        return "/board/updateForm";
    }
    @GetMapping("/detail/{boardNo}")
    public String boardDetail(@PathVariable long boardNo, Model model){
        model.addAttribute("board",vtwBoardService.selectOne(boardNo));
        return "board/boardDetail";
    }

    @GetMapping("/mypage")
    public String userPage(PrincipalDetail principal
            , Model model){
        return "member/myPage";
    }

}
