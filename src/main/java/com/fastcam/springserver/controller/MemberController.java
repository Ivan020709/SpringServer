package com.fastcam.springserver.controller;

import com.fastcam.springserver.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {
    
    @Autowired
    MemberService ms;




}
