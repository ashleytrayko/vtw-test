package com.example.vtw.controller;

import com.example.vtw.configuration.PrincipalDetail;
import com.example.vtw.service.BoardService;
import com.example.vtw.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// 단순 View만 처리하는 Controller로 수정
@org.springframework.stereotype.Controller
public class Controller {
    

//    필드 의존성 주입으로 순환참조 문제 발생가능 -> 실제로 발생함. 생성자 의존성 주입으로 수정
//    @Autowired
//    private VtwService vtwService;

    private final UserService userService;
    private final BoardService boardService;

    public Controller(UserService userService, BoardService boardService){
        this.userService = userService;
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String mainPage(
            Model model,
            @PageableDefault(size = 10, sort = "boardNo", direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("boards", boardService.boardList(pageable));
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
        model.addAttribute("board", boardService.selectOne(boardNo));
        return "/board/updateForm";
    }
    @GetMapping("/detail/{boardNo}")
    public String boardDetail(@PathVariable long boardNo, Model model){
        model.addAttribute("board", boardService.selectOne(boardNo));
        return "board/boardDetail";
    }

    @GetMapping("/mypage")
    public String userPage(PrincipalDetail principal
            , Model model){
        return "member/myPage";
    }

}
