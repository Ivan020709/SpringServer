package com.fastcam.springserver.controller;

import com.fastcam.springserver.dto.BoardCommentRequest;
import com.fastcam.springserver.entity.BoardComment;
import com.fastcam.springserver.security.SessionUserResolver;
import com.fastcam.springserver.service.BoardCommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board")
public class BoardCommentController {
    private final BoardCommentService service;
    private final SessionUserResolver sessionUsers;

    public BoardCommentController(BoardCommentService service, SessionUserResolver sessionUsers) {
        this.service = service;
        this.sessionUsers = sessionUsers;
    }

    @GetMapping("/{boardId}/comments")
    public Map<String, Object> list(@PathVariable int boardId, HttpSession session) {
        List<BoardComment> comments = service.list(boardId, sessionUsers.optionalUserId(session));
        return Map.of("comments", comments, "count", comments.size());
    }

    @PostMapping("/{boardId}/comments")
    public Map<String, Object> create(@PathVariable int boardId, @RequestBody BoardCommentRequest request,
                                      HttpSession session) {
        return Map.of("comment", service.create(boardId, sessionUsers.requireUserId(session), request.getContent()));
    }

    @PutMapping("/comments/{commentId}")
    public Map<String, Object> update(@PathVariable int commentId, @RequestBody BoardCommentRequest request,
                                      HttpSession session) {
        return Map.of("comment", service.update(commentId, sessionUsers.requireUserId(session), request.getContent()));
    }

    @DeleteMapping("/comments/{commentId}")
    public Map<String, Object> delete(@PathVariable int commentId, HttpSession session) {
        service.delete(commentId, sessionUsers.requireUserId(session));
        return Map.of("msg", "OK");
    }
}
