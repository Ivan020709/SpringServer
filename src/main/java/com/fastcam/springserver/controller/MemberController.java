package com.fastcam.springserver.controller;

import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/member")
public class MemberController {
    
    @Autowired
    MemberService ms;





}
