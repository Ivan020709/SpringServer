package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Integer> {
    Page<NoticeBoard> findAllByTitleContaining(String key, Pageable pageable);

    List<NoticeBoard> findByContentContaining(String key);

    Page<NoticeBoard> findAllByContentContaining(String key, Pageable pageable);

    List<NoticeBoard> findByTitleContaining(String key);

    List<NoticeBoard> findByFixedOrderByIndateAsc(String y);
}
