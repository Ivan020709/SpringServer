package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Integer> {
    List<BoardComment> findAllByBoardIdOrderByCreatedAtAsc(int boardId);
    long countByBoardId(int boardId);
    void deleteAllByBoardId(int boardId);
}
