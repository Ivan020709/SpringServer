package com.fastcam.springserver.controller;

import com.fastcam.springserver.entity.EmotionDiary;
import com.fastcam.springserver.service.EmotionDiaryService;
import com.fastcam.springserver.security.SessionUserResolver;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

@RestController
@RequestMapping("/diary")
public class EmotionDiaryController {
    @Autowired private EmotionDiaryService service;
    @Autowired private SessionUserResolver sessionUsers;

    @GetMapping("/shared")
    public HashMap<String, Object> shared() { return service.getShared(); }

    @GetMapping("/mine")
    public HashMap<String, Object> mine(HttpSession session) {
        return service.getMine(sessionUsers.requireUserId(session));
    }

    @GetMapping("/mine/{userId}")
    public HashMap<String, Object> legacyMine(@PathVariable int userId, HttpSession session) {
        int loginUserId = sessionUsers.requireUserId(session);
        if (loginUserId != userId) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "본인의 일기만 조회할 수 있습니다.");
        }
        return service.getMine(loginUserId);
    }

    @PostMapping
    public HashMap<String, Object> create(@RequestBody EmotionDiary diary, HttpSession session) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("diary", service.save(diary, sessionUsers.requireUserId(session)));
        return result;
    }
}
