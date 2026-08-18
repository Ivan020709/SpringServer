package com.fastcam.springserver.service;

import com.fastcam.springserver.entity.EmotionDiary;
import com.fastcam.springserver.repository.EmotionDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;

@Service
@Transactional
public class EmotionDiaryService {
    @Autowired private EmotionDiaryRepository repository;

    @Transactional(readOnly = true)
    public HashMap<String, Object> getShared() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("diaries", repository.findBySharedTrueOrderByDiaryDateDescCreatedAtDesc(PageRequest.of(0, 12)));
        return result;
    }

    @Transactional(readOnly = true)
    public HashMap<String, Object> getMine(int userId) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("diaries", repository.findByUserIdOrderByDiaryDateDesc(userId));
        return result;
    }

    public EmotionDiary save(EmotionDiary diary) { return repository.save(diary); }
}
