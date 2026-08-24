package com.fastcam.springserver.service;

import com.fastcam.springserver.dto.ResponseDto;
import com.fastcam.springserver.entity.EmotionDiary;
import com.fastcam.springserver.repository.EmotionDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.sql.Date;

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

        List<EmotionDiary> diaryList =
                repository.findByUserIdOrderByDiaryDateDesc(userId);

        result.put("msg", "OK");

        // 다른 감정일기 화면에서 사용 중인 이름
        result.put("diaries", diaryList);

        // 현재 Diary.js가 확인하는 이름
        result.put("data", diaryList);

        return result;
    }

    @Transactional(readOnly = true)
    public HashMap<String, Object> getCalendar(
            int userId,
            String startDate,
            String endDate
    ) {
        HashMap<String, Object> result = new HashMap<>();

        List<EmotionDiary> diaryList =
                repository.findByUserIdAndDiaryDateBetweenOrderByDiaryDateAsc(
                        userId,
                        Date.valueOf(startDate),
                        Date.valueOf(endDate)
                );

        result.put("msg", "OK");
        result.put("diaries", diaryList);

        return result;
    }

    public EmotionDiary save(EmotionDiary diary, int userId) {
        diary.setId(null);
        diary.setUserId(userId);

        return repository.save(diary);
    }

    public HashMap<String, Object> updateShare(
            int diaryId,
            int userId,
            boolean shared
    ) {
        HashMap<String, Object> result = new HashMap<>();

        EmotionDiary diary =
                repository.findByIdAndUserId(
                        diaryId,
                        userId
                );

        if (diary == null) {
            result.put("msg", "FAIL");
            return result;
        }

        diary.setShared(shared);

        EmotionDiary savedDiary =
                repository.save(diary);

        result.put("msg", "OK");
        result.put("diary", savedDiary);

        return result;
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
                new Date(System.currentTimeMillis())
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