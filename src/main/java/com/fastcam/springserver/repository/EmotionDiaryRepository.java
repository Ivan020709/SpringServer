package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.EmotionDiary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmotionDiaryRepository extends JpaRepository<EmotionDiary, Integer> {
    List<EmotionDiary> findBySharedTrueOrderByDiaryDateDescCreatedAtDesc(Pageable pageable);
    List<EmotionDiary> findByUserIdOrderByDiaryDateDesc(int userId);
}
