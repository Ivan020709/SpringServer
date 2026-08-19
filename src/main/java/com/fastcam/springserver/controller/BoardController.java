package com.fastcam.springserver.controller;

import com.fastcam.springserver.dto.BoardReportRequest;
import com.fastcam.springserver.entity.Board;
import com.fastcam.springserver.security.SessionUserResolver;
import com.fastcam.springserver.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/board")
public class BoardController {
    private final BoardService boards;
    private final SessionUserResolver sessionUsers;

    public BoardController(BoardService boards, SessionUserResolver sessionUsers) {
        this.boards = boards;
        this.sessionUsers = sessionUsers;
    }

    @GetMapping("/getBoard/{boardnum}")
    public HashMap<String, Object> getBoard(@PathVariable int boardnum, HttpSession session) {
        return boards.getBoardDetail(boardnum, sessionUsers.optionalUserId(session));
    }

    @GetMapping("/getBoardList/{page}")
    public HashMap<String, Object> getBoardList(@PathVariable int page,
                                                @RequestParam(defaultValue = "latest") String sort,
                                                HttpSession session) {
        return boards.getBoardList(page, sort, sessionUsers.optionalUserId(session));
    }

    @PostMapping("/toggleLike")
    public HashMap<String, Object> toggleLike(@RequestParam int boardId, HttpSession session) {
        return boards.toggleLike(boardId, sessionUsers.requireUserId(session));
    }

    @PostMapping("/{boardId}/report")
    public Map<String, Object> reportBoard(@PathVariable int boardId, @RequestBody BoardReportRequest request,
                                           HttpSession session) {
        boards.reportBoard(boardId, sessionUsers.requireUserId(session), request.getReason(), request.getDetail());
        return Map.of("msg", "OK");
    }

    @PostMapping("/reportBoard")
    public Map<String, Object> legacyReportBoard(@RequestBody HashMap<String, Object> request, HttpSession session) {
        int boardId = ((Number) request.get("boardId")).intValue();
        boards.reportBoard(boardId, sessionUsers.requireUserId(session), (String) request.get("reason"),
                (String) request.getOrDefault("detail", ""));
        return Map.of("msg", "OK");
    }

    @PostMapping("/plusCount")
    public Map<String, Object> plusCount(@RequestParam int boardnum) {
        boards.plusCount(boardnum);
        return Map.of("msg", "OK");
    }

    @PostMapping("/updateBoard")
    public Map<String, Object> updateBoard(@RequestBody Board board, HttpSession session) {
        boards.updateBoard(board, sessionUsers.requireUserId(session));
        return Map.of("msg", "OK");
    }

    @DeleteMapping("/deleteBoard/{boardnum}")
    public Map<String, Object> deleteBoard(@PathVariable int boardnum, HttpSession session) {
        boards.deleteBoard(boardnum, sessionUsers.requireUserId(session));
        return Map.of("msg", "OK");
    }

    @PostMapping("/insertBoard")
    public Map<String, Object> insertBoard(@RequestBody Board board, HttpSession session) {
        return Map.of("msg", "OK", "board", boards.insertBoard(board, sessionUsers.requireUserId(session)));
    }
}
