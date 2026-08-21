package com.fastcam.springserver.controller;

import com.fastcam.springserver.entity.EmotionDiary;
import com.fastcam.springserver.service.EmotionDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

@RestController
@RequestMapping("/diary")
public class EmotionDiaryController {
    @Autowired private EmotionDiaryService service;

    @GetMapping("/shared")
    public HashMap<String, Object> shared() { return service.getShared(); }

    @GetMapping("/mine/{userId}")
    public HashMap<String, Object> mine(@PathVariable int userId) {
        return service.getMine(userId);
    }

    @GetMapping("/calendar/{userId}")
    public HashMap<String, Object> calendar(
            @PathVariable int userId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ) {
        return service.getCalendar(
                userId,
                startDate,
                endDate
        );
    }

    @PostMapping
    public HashMap<String, Object> create(@RequestBody EmotionDiary diary) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("diary", service.save(diary, diary.getUserId()));
        return result;
    }

}
