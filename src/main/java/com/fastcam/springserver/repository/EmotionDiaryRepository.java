package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.EmotionDiary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface EmotionDiaryRepository extends JpaRepository<EmotionDiary, Integer> {

    List<EmotionDiary> findBySharedTrueOrderByDiaryDateDescCreatedAtDesc(
            Pageable pageable
    );

    List<EmotionDiary> findByUserIdOrderByDiaryDateDesc(
            int userId
    );

    // 특정 사용자의 시작일~종료일 감정일기 조회
    List<EmotionDiary> findByUserIdAndDiaryDateBetweenOrderByDiaryDateAsc(
            int userId,
            Date startDate,
            Date endDate
    );

    // 본인이 작성한 감정일기인지 확인하면서 조회
    EmotionDiary findByIdAndUserId(
            int id,
            int userId
    );
}