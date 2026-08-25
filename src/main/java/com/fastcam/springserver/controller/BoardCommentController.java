package com.fastcam.springserver.controller;

import com.fastcam.springserver.dto.BoardCommentRequest;
import com.fastcam.springserver.service.BoardCommentService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/board")
public class BoardCommentController {
    private final BoardCommentService service;

    public BoardCommentController(BoardCommentService service) {
        this.service = service;
    }

    @GetMapping("/{boardId}/comments")
    public Map<String, Object> list(@PathVariable int boardId,
                                    @RequestParam(required = false) Integer userId) {
        java.util.List<HashMap<String, Object>> comments = service.list(boardId, userId);
        return Map.of("comments", comments, "count", comments.size());
    }

    @PostMapping("/{boardId}/comments")
    public Map<String, Object> create(@PathVariable int boardId, @RequestParam int userId,
                                      @RequestBody BoardCommentRequest request) {
        return Map.of("comment", service.create(boardId, userId, request.getContent()));
    }

    // PUT 대신 POST로 댓글을 수정합니다.
    @PostMapping("/comments/{commentId}/update")
    public Map<String, Object> update(@PathVariable int commentId, @RequestParam int userId,
                                      @RequestBody BoardCommentRequest request) {
        return Map.of("comment", service.update(commentId, userId, request.getContent()));
    }

    @DeleteMapping("/comments/{commentId}")
    public Map<String, Object> delete(@PathVariable int commentId, @RequestParam int userId) {
        service.delete(commentId, userId);
        return Map.of("msg", "OK");
    }

}
