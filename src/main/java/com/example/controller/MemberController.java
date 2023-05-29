package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.ClassProduct;
import com.example.dto.Member;
import com.example.service.classproduct.ClassManageService;
import com.example.service.member.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value = "/member")
public class MemberController {

    final String format = "MemberController => {}";

    @Autowired MemberService mService;
    @Autowired ClassManageService cService;

    BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();

    @GetMapping(value = "/join.do")
    public String joinGET(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);
        
        return "/member/join";

    }

    @PostMapping(value = "/join.do")
    public String joinPOST(@ModelAttribute Member obj, HttpSession httpSession) {

        log.info(format, obj.getPassword());

        obj.setPassword(bcpe.encode(obj.getPassword()));

        int ret = mService.insertMemberOne(obj);

        log.info(format, ret);

        if(ret == 1) {
            // httpSession.setAttribute("alertMessage", "회원 가입 완료했습니다.");
            // httpSession.setAttribute("alertUrl", "/home.do");
            return "redirect:joinsuccess.do";
        }
        else {
            return "redirect:/member/join.do";
        }

    }

    @GetMapping(value = "/joinsuccess.do")
    public String joinsuccessGET() {

        return "/member/joinsuccess";
    }

    @GetMapping(value = "/mypage.do")
    public String mypageGET(
        @RequestParam(name = "menu", defaultValue = "0") int menu,
        @AuthenticationPrincipal User user,
        Model model) {

        String id = user.getUsername();

        if(menu == 0) {
            return "redirect:/member/mypage.do?menu=1";
        }

        if(menu == 1) {
            List<ClassProduct> list = cService.selectMyClassList(id);
            //log.info(format, list.toString());
        }

        else if(menu == 2) {

        }

        else if(menu == 3) {

        }

        else if(menu == 4) {
            Member obj = mService.selectMemberOne(id);
            //slog.info(format, obj.toString());
            model.addAttribute("obj", obj);
        }

        model.addAttribute("user", user);

        return "/member/mypage/mypage";

    }

    @PostMapping(value = "/mypage.do")
    public String mypagePOST(
        @RequestParam(name = "menu", defaultValue = "0", required = false) int menu
    ) {

        return "redirect:/mypage.do?menu="+menu;
        
    }


    @GetMapping(value = "/myclass.do")
    public String myclassGET(
        @RequestParam(name = "menu", defaultValue = "0") int menu,
        @AuthenticationPrincipal User user,
        Model model
    ) {
        String id = user.getUsername();

        if(menu == 0) {
            // return "member/myclass_menu1";
            return "redirect:/member/myclass.do?menu=1";
        }

        if(menu == 1) {
            List<ClassProduct> list = cService.selectMyClassList(id);
            model.addAttribute("list", list);
            //log.info(format, list.toString());
            // return "member/myclass_menu1";
        }

        else if(menu == 2) {

        }

        model.addAttribute("user", user);
        return "/member/myclass";
    }

    @PostMapping(value = "/myclass.do")
    public String myclassPOST(
        @RequestParam(name = "menu", defaultValue = "0", required = false) int menu
    ){
        return "redirect:/myclass.do?menu="+menu;
    }
    
}
