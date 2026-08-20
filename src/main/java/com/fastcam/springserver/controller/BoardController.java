package com.fastcam.springserver.controller;

import com.fastcam.springserver.dto.BoardReportRequest;
import com.fastcam.springserver.entity.Board;
import com.fastcam.springserver.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/board")
public class BoardController {
    private final BoardService boards;

    public BoardController(BoardService boards) {
        this.boards = boards;
    }

    @GetMapping("/getBoard/{boardnum}")
    public HashMap<String, Object> getBoard(@PathVariable int boardnum,
                                             @RequestParam(required = false) Integer userId) {
        return boards.getBoardDetail(boardnum, userId);
    }

    @GetMapping("/getBoardList/{page}")
    public HashMap<String, Object> getBoardList(@PathVariable int page,
                                                @RequestParam(defaultValue = "latest") String sort,
                                                @RequestParam(required = false) Integer userId) {
        return boards.getBoardList(page, sort, userId);
    }

    @PostMapping("/toggleLike")
    public HashMap<String, Object> toggleLike(@RequestParam int boardId, @RequestParam int userId) {
        return boards.toggleLike(boardId, userId);
    }

    @PostMapping("/{boardId}/report")
    public Map<String, Object> reportBoard(@PathVariable int boardId, @RequestParam int userId,
                                           @RequestBody BoardReportRequest request) {
        boards.reportBoard(boardId, userId, request.getReason(), request.getDetail());
        return Map.of("msg", "OK");
    }

    @PostMapping("/reportBoard")
    public Map<String, Object> legacyReportBoard(@RequestBody HashMap<String, Object> request) {
        int boardId = ((Number) request.get("boardId")).intValue();
        int reporterId = ((Number) request.get("reporterId")).intValue();
        boards.reportBoard(boardId, reporterId, (String) request.get("reason"),
                (String) request.getOrDefault("detail", ""));
        return Map.of("msg", "OK");
    }

    @PostMapping("/plusCount")
    public Map<String, Object> plusCount(@RequestParam int boardnum) {
        boards.plusCount(boardnum);
        return Map.of("msg", "OK");
    }

    @PostMapping("/updateBoard")
    public Map<String, Object> updateBoard(@RequestBody Board board) {
        boards.updateBoard(board, board.getUserid());
        return Map.of("msg", "OK");
    }

    @DeleteMapping("/deleteBoard/{boardnum}")
    public Map<String, Object> deleteBoard(@PathVariable int boardnum, @RequestParam int userId) {
        boards.deleteBoard(boardnum, userId);
        return Map.of("msg", "OK");
    }

    @PostMapping("/insertBoard")
    public Map<String, Object> insertBoard(@RequestBody Board board) {
        return Map.of("msg", "OK", "board", boards.insertBoard(board, board.getUserid()));
    }
}
