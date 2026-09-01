package com.fastcam.springserver.controller;

import com.fastcam.springserver.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/sms")
public class SMSController {

    @Autowired
    SMSService ss;
    private int number;
    @PostMapping("/sendSMS")
    public HashMap<String, Object> sendMail(@RequestParam("phone")String phone){
        HashMap<String, Object> result = new HashMap<>();
        number = ss.sendSMS(phone);
        result.put("msg", "ok");
        return result;
    }

    @PostMapping("/confirmNumber")
    public HashMap<String, Object> confirmNumber(@RequestParam("usernumber")String usernumber){
        HashMap<String, Object> result = new HashMap<>();
        String num = String.valueOf(number);
        if(num.equals(usernumber))
            result.put("msg", "ok");
        else
            result.put("msg", "not_ok");

        return result;
    }
}
