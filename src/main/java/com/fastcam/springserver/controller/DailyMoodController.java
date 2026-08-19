package com.fastcam.springserver.controller;

import com.fastcam.springserver.dto.DailyMoodRequest;
import com.fastcam.springserver.entity.DailyMood;
import com.fastcam.springserver.service.DailyMoodService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/daily-mood")
public class DailyMoodController {
    private final DailyMoodService service;

    public DailyMoodController(DailyMoodService service) {
        this.service = service;
    }

    @GetMapping("/today")
    public Map<String, Object> getToday(HttpSession session) {
        return service.getToday(requireLogin(session));
    }

    @PostMapping("/today")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createToday(@RequestBody DailyMoodRequest request, HttpSession session) {
        DailyMood dailyMood = service.createToday(requireLogin(session), request.getMood());
        return Map.of("selected", true, "mood", dailyMood.getMood());
    }

    private int requireLogin(HttpSession session) {
        Object userId = session.getAttribute("loginUserId");
        if (!(userId instanceof Integer)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return (Integer) userId;
    }
}
