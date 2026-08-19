package com.fastcam.springserver.service;

import com.fastcam.springserver.entity.BoardComment;
import com.fastcam.springserver.entity.Board;
import com.fastcam.springserver.repository.BoardCommentRepository;
import com.fastcam.springserver.repository.BoardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class BoardCommentService {
    private final BoardCommentRepository comments;
    private final BoardRepository boards;

    public BoardCommentService(BoardCommentRepository comments, BoardRepository boards) {
        this.comments = comments;
        this.boards = boards;
    }

    @Transactional(readOnly = true)
    public List<BoardComment> list(int boardId, Integer userId) {
        requireVisibleBoard(boardId, userId);
        return comments.findAllByBoardIdOrderByCreatedAtAsc(boardId);
    }

    public BoardComment create(int boardId, int userId, String content) {
        requireVisibleBoard(boardId, userId);
        BoardComment comment = new BoardComment();
        comment.setBoardId(boardId);
        comment.setUserId(userId);
        comment.setContent(requireContent(content));
        return comments.save(comment);
    }

    public BoardComment update(int commentId, int userId, String content) {
        BoardComment comment = requireComment(commentId);
        requireOwner(comment, userId);
        comment.setContent(requireContent(content));
        return comment;
    }

    public void delete(int commentId, int userId) {
        BoardComment comment = requireComment(commentId);
        requireOwner(comment, userId);
        comments.delete(comment);
    }

    private Board requireVisibleBoard(int boardId, Integer userId) {
        Board board = boards.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        if (board.isIsprivate() && (userId == null || board.getUserid() != userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비공개 게시글입니다.");
        }
        return board;
    }

    private BoardComment requireComment(int commentId) {
        return comments.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));
    }

    private void requireOwner(BoardComment comment, int userId) {
        if (comment.getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 댓글만 변경할 수 있습니다.");
        }
    }

    private String requireContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 내용을 입력해주세요.");
        }
        return content.trim();
    }
}
