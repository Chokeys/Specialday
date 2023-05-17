package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.config.KakaoLocalAPI;
import com.example.dto.ActivityCate;
import com.example.dto.CityCate;
import com.example.dto.ClassProduct;
import com.example.service.classproduct.ClassInsertService;
import com.example.service.classproduct.ClassSelectService;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping(value = "/class")
@Slf4j
public class ClassController {

    final String format = "ClassController => {}";

    @Autowired ClassSelectService cService;
    @Autowired ClassInsertService iService;

    @GetMapping(value = "select.do")
    public String selectGET(@RequestParam(name = "search", defaultValue = "", required = false) String search, Model model) {

        if(search.equals("")) {
            return "redirect:/class/select.do?search=list";
        }

        List<ActivityCate> list1 = cService.selectActivityCateList();
        List<CityCate> list2 = cService.selectCityCateList();
        model.addAttribute("list1", list1);
        model.addAttribute("list2", list2);

        return "/class/select";
    }

    @GetMapping(value = "insert.do")
    public String insertGET(Model model) {

        model.addAttribute("actlist", cService.selectActivityCateList());
        model.addAttribute("citylist", cService.selectCityCateList());

        return "/class/insert";
    }

    @PostMapping(value = "insert.do")
    public String insertPOST(
        @ModelAttribute ClassProduct obj
    ) {

        Map<String, String> map = KakaoLocalAPI.getCoordinate(obj.getAddress1());
        obj.setLatitude(map.get("x"));
        obj.setLongitude(map.get("y"));
        //obj.setMemberid(id); <- 아이디 추가 필요! (security session에 저장된 ID 정보를 호출)

        log.info(format, obj.toString());
        
        int ret = iService.insertClassOne(obj);

        if(ret==1) {
            return "redirect:/member/mypage.do?menu=";
        }
        else {
            return "redirect:/class/insert.do";
        }
        
    }

    @GetMapping(value = "/unit.do")
    public String unitGET() {

        return "/class/unit";
    }



    
}