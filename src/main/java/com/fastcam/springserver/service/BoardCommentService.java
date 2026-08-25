package com.fastcam.springserver.service;

import com.fastcam.springserver.entity.BoardComment;
import com.fastcam.springserver.entity.Board;
import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.repository.BoardCommentRepository;
import com.fastcam.springserver.repository.BoardRepository;
import com.fastcam.springserver.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class BoardCommentService {
    private final BoardCommentRepository comments;
    private final BoardRepository boards;
    private final MemberRepository members;

    public BoardCommentService(
            BoardCommentRepository comments,
            BoardRepository boards,
            MemberRepository members
    ) {
        this.comments = comments;
        this.boards = boards;
        this.members = members;
    }

    @Transactional(readOnly = true)
    public List<HashMap<String, Object>> list(int boardId, Integer userId) {
        requireVisibleBoard(boardId, userId);
        return comments.findAllByBoardIdOrderByCreatedAtAsc(boardId)
                .stream()
                .map(this::toCommentData)
                .toList();
    }

    public HashMap<String, Object> create(int boardId, int userId, String content) {
        requireVisibleBoard(boardId, userId);
        requireMember(userId);
        BoardComment comment = new BoardComment();
        comment.setBoardId(boardId);
        comment.setUserId(userId);
        comment.setContent(requireContent(content));
        return toCommentData(comments.save(comment));
    }

    public HashMap<String, Object> update(int commentId, int userId, String content) {
        BoardComment comment = requireComment(commentId);
        requireOwner(comment, userId);
        comment.setContent(requireContent(content));
        return toCommentData(comments.save(comment));
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

    // 댓글 데이터에 작성자 이름을 추가하여 프론트로 보냅니다.
    private HashMap<String, Object> toCommentData(BoardComment comment) {
        HashMap<String, Object> data = new HashMap<>();
        Member member = members.findByUserid(comment.getUserId());

        data.put("id", comment.getId());
        data.put("boardId", comment.getBoardId());
        data.put("userId", comment.getUserId());
        data.put("userName", member != null ? member.getName() : "알 수 없음");
        data.put("content", comment.getContent());
        data.put("createdAt", comment.getCreatedAt());
        return data;
    }

    private Member requireMember(int userId) {
        Member member = members.findByUserid(userId);
        if (member == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "로그인 회원을 찾을 수 없습니다."
            );
        }
        return member;
    }
}
