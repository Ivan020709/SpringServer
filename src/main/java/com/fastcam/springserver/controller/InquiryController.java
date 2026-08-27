package com.fastcam.springserver.controller;

import com.fastcam.springserver.dto.InquiryCommentRequest;
import com.fastcam.springserver.entity.Inquiry;
import com.fastcam.springserver.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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


    // 선택한 문의글의 댓글 목록을 조회합니다.
    @GetMapping("/{inquiryId}/comments")
    public HashMap<String, Object> getInquiryComments(@PathVariable int inquiryId) {
        HashMap<String, Object> result = new HashMap<>();
        List<HashMap<String, Object>> comments = inquiryService.getInquiryComments(inquiryId);
        result.put("comments", comments);
        result.put("count", comments.size());
        result.put("msg", "OK");
        return result;
    }


    // 실제 inquiry_comment 테이블 구조에 맞춰 작성자 닉네임과 내용을 저장합니다.
    @PostMapping("/{inquiryId}/comments")
    public HashMap<String, Object> insertInquiryComment(
            @PathVariable int inquiryId,
            @RequestParam String nickname,
            @RequestBody InquiryCommentRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("comment", inquiryService.insertInquiryComment(inquiryId, nickname, request.getContent()));
        result.put("msg", "OK");
        return result;
    }


    // PUT을 사용하지 않고 POST로 문의 댓글을 수정합니다.
    @PostMapping("/comments/{commentId}/update")
    public HashMap<String, Object> updateInquiryComment(
            @PathVariable int commentId,
            @RequestBody InquiryCommentRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("comment", inquiryService.updateInquiryComment(commentId, request.getContent()));
        result.put("msg", "OK");
        return result;
    }


    @DeleteMapping("/comments/{commentId}")
    public HashMap<String, Object> deleteInquiryComment(@PathVariable int commentId) {
        inquiryService.deleteInquiryComment(commentId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("msg", "OK");
        return result;
    }
}
