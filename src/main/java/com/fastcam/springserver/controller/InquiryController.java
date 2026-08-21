package com.fastcam.springserver.controller;

import com.fastcam.springserver.entity.Inquiry;
import com.fastcam.springserver.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/inquiry")
public class InquiryController {

    @Autowired
    InquiryService inquiryService;

    @GetMapping("/getInquiryList/{page}")
    public HashMap<String, Object> getInquiryList(
            @PathVariable int page) {

        return inquiryService.getInquiryList(page);
    }

    @GetMapping("/getInquiry/{inquirynum}")
    public HashMap<String, Object> getInquiry(
            @PathVariable int inquirynum) {

        HashMap<String, Object> result = new HashMap<>();

        Inquiry inquiry =
                inquiryService.getInquiry(inquirynum);

        if (inquiry == null) {
            result.put("msg", "FAIL");
        } else {
            result.put("msg", "OK");
            result.put("inquiry", inquiry);
        }

        return result;
    }

    @PostMapping("/insertInquiry")
    public HashMap<String, Object> insertInquiry(
            @RequestBody Inquiry inquiry) {

        HashMap<String, Object> result = new HashMap<>();

        inquiryService.insertInquiry(inquiry);
        result.put("msg", "OK");

        return result;
    }

    @PostMapping("/updateInquiry")
    public HashMap<String, Object> updateInquiry(
            @RequestBody Inquiry inquiry) {

        HashMap<String, Object> result = new HashMap<>();

        boolean success =
                inquiryService.updateInquiry(inquiry);

        if (success) {
            result.put("msg", "OK");
        } else {
            result.put("msg", "FAIL");
        }

        return result;
    }

    @DeleteMapping("/deleteInquiry/{inquirynum}")
    public HashMap<String, Object> deleteInquiry(
            @PathVariable int inquirynum) {

        HashMap<String, Object> result = new HashMap<>();

        boolean success =
                inquiryService.deleteInquiry(inquirynum);

        if (success) {
            result.put("msg", "OK");
        } else {
            result.put("msg", "FAIL");
        }

        return result;
    }
}