package com.fastcam.springserver.service;

import com.fastcam.springserver.dto.ResponseDto;
import com.fastcam.springserver.entity.EmotionDiary;
import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.repository.EmotionDiaryRepository;
import com.fastcam.springserver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.sql.Date;

@Service
@Transactional
public class EmotionDiaryService {

    @Autowired
    private EmotionDiaryRepository repository;

    // 감정일기의 userId로 회원 닉네임을 조회하기 위해 사용합니다.
    @Autowired
    private MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public HashMap<String, Object> getShared() {

        HashMap<String, Object> result = new HashMap<>();

        // DB에서 공유 상태인 감정일기를 최신순으로 12개 조회합니다.
        List<EmotionDiary> diaryList =
                repository.findBySharedTrueOrderByDiaryDateDescCreatedAtDesc(
                        PageRequest.of(0, 12)
                );

        // 감정일기 정보와 작성자 닉네임을 함께 담을 목록입니다.
        List<HashMap<String, Object>> sharedDiaryList =
                new ArrayList<>();

        for (EmotionDiary diary : diaryList) {

            HashMap<String, Object> diaryData =
                    new HashMap<>();

            // 감정일기에 저장된 userId로 회원 정보를 조회합니다.
            Member member =
                    memberRepository.findByUserid(
                            diary.getUserId()
                    );

            // 기존 감정일기 정보를 하나씩 담습니다.
            diaryData.put("id", diary.getId());
            diaryData.put("userId", diary.getUserId());
            diaryData.put("diaryDate", diary.getDiaryDate());
            diaryData.put("mood", diary.getMood());
            diaryData.put("emoji", diary.getEmoji());
            diaryData.put("content", diary.getContent());
            diaryData.put("summary", diary.getSummary());
            diaryData.put("shared", diary.isShared());
            diaryData.put("createdAt", diary.getCreatedAt());

            // 회원이 존재하면 닉네임을 보내고, 없으면 익명으로 표시합니다.
            if (member != null) {
                diaryData.put("nickname", member.getNickname());
            } else {
                diaryData.put("nickname", "익명");
            }

            sharedDiaryList.add(diaryData);
        }

        // SharedDiary.js가 확인하는 diaries라는 이름으로 전달합니다.
        result.put("diaries", sharedDiaryList);
        result.put("msg", "OK");

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

    // DB에 저장된 일기 번호를 AIController에서 사용할 수 있도록
    // void 대신 저장된 EmotionDiary 객체를 반환합니다.
    public EmotionDiary saveFromAiResult(
            int userid,
            ResponseDto response
    ) {

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

        // repository.save()가 반환하는 객체에는 DB에서 만든 id가 들어 있습니다.
        return repository.save(diary);
    }
}
