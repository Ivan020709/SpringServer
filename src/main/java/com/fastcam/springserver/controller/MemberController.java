package com.fastcam.springserver.controller;

import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/member")
public class MemberController {
    
    @Autowired
    MemberService ms;


    @PostMapping("/login")
    public HashMap<String, Object>login(
            @RequestParam("email") String email,
            @RequestParam("pwd") String pwd
    ){
        HashMap<String, Object> map = new HashMap<String, Object>();
        Member mdto = ms.getMember(email);

        if(mdto ==null){
            map.put("msg","notOK" );
            return map;
        } else if(!mdto.getPwd().equals(pwd)){
            map.put("msg","notOK");
            return map;
        }else{
            map.put("msg", "OK");
            map.put("loginUser",mdto);
        }
        return map;
    }

    @PostMapping("/join")
    public HashMap<String, Object>join(@RequestBody Member member){
        HashMap<String, Object> map = new HashMap<>();
        ms.insertMember(member);
        map.put("msg", "OK");
        return map;

    }



}
