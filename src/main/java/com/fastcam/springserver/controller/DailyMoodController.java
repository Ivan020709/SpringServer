package com.fastcam.springserver.controller;

import com.fastcam.springserver.dto.DailyMoodRequest;
import com.fastcam.springserver.entity.DailyMood;
import com.fastcam.springserver.service.DailyMoodService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/daily-mood")
public class DailyMoodController {
    private final DailyMoodService service;

    public DailyMoodController(DailyMoodService service) {
        this.service = service;
    }

    @GetMapping("/today")
    public Map<String, Object> getToday(@RequestParam int userId) {
        return service.getToday(userId);
    }

    @PostMapping("/today")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createToday(@RequestParam int userId, @RequestBody DailyMoodRequest request) {
        DailyMood dailyMood = service.createToday(userId, request.getMood());
        return Map.of("selected", true, "mood", dailyMood.getMood());
    }
}
