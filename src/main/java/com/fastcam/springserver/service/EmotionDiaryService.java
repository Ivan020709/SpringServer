package com.fastcam.springserver.service;

import com.fastcam.springserver.dto.ResponseDto;
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

    @Autowired
    private EmotionDiaryRepository repository;

    @Transactional(readOnly = true)
    public HashMap<String, Object> getShared() {
        HashMap<String, Object> result = new HashMap<>();

        result.put(
                "diaries",
                repository.findBySharedTrueOrderByDiaryDateDescCreatedAtDesc(
                        PageRequest.of(0, 12)
                )
        );

        return result;
    }

    @Transactional(readOnly = true)
    public HashMap<String, Object> getMine(int userId) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(
                "diaries",
                repository.findByUserIdOrderByDiaryDateDesc(userId)
        );

        return result;
    }

    public EmotionDiary save(EmotionDiary diary, int userId) {
        diary.setId(null);
        diary.setUserId(userId);

        return repository.save(diary);
    }


    // =====================================================
    // AI 분석 결과 → 감정일기 DB 저장
    // =====================================================

    public void saveFromAiResult(int userid, ResponseDto response) {

        EmotionDiary diary = new EmotionDiary();

        // 사용자 ID
        diary.setUserId(userid);
        // 일기 날짜
        diary.setDiaryDate(
                new java.sql.Date(System.currentTimeMillis())
        );
        // 감정
        if (response.getEmotion() != null) {
            diary.setMood(
                    response.getEmotion().getMain_emotion()
            );
        } else {
            diary.setMood("알 수 없음");
        }
        // AI 요약
        diary.setSummary(response.getSummary());
        // AI가 작성한 일기
        diary.setContent(response.getDiary());
        // 처음에는 비공개
        diary.setShared(false);
        // AI가 분석한 이모지
        diary.setEmoji(response.getEmotion().getEmoji());

        // DB 저장
        repository.save(diary);
    }
}