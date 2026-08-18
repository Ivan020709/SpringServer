package com.fastcam.springserver.controller;

import com.fastcam.springserver.entity.Board;
import com.fastcam.springserver.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardService bs;


    @GetMapping("/getBoard/{boardnum}")
    public HashMap<String, Object> getBoard(@PathVariable("boardnum") int boardnum){
        HashMap<String, Object> map = new HashMap<>();
        Board bdto = bs.getBoard(boardnum);
        map.put("board", bdto);
        return map;
    }

    @GetMapping("/getBoardList/{page}")
    public HashMap<String, Object> getBoardList(@PathVariable("page") int page, @RequestParam(value = "userId", required = false) Integer userId){
        HashMap<String, Object> map = bs.getBoardList(page, userId);
        return map;
    }

    @PostMapping("/toggleLike")
    public HashMap<String, Object> toggleLike(@RequestParam int boardId, @RequestParam int userId) {
        return bs.toggleLike(boardId, userId);
    }

    @PostMapping("/reportBoard")
    public HashMap<String, Object> reportBoard(@RequestBody HashMap<String, Object> request) {
        int boardId = ((Number) request.get("boardId")).intValue();
        int reporterId = ((Number) request.get("reporterId")).intValue();
        bs.reportBoard(boardId, reporterId, (String) request.get("reason"), (String) request.getOrDefault("detail", ""));
        HashMap<String, Object> result = new HashMap<>(); result.put("msg", "OK"); return result;
    }


    @PostMapping("/plusCount")
    public HashMap<String, Object> plusCount( @RequestParam("boardnum") int boardnum){
        HashMap<String, Object> map = new HashMap<String, Object>();
        bs.plusCount( boardnum );
        map.put("msg", "OK");
        return map;
    }

    @PostMapping("/updateBoard")
    public HashMap<String, Object> updateBoard(@RequestBody Board board){
        HashMap<String, Object>map = new HashMap<>();
        bs.updateBoard(board);
        map.put("msg", "OK");
        return map;
    }


    @DeleteMapping("/deleteBoard/{boardnum}")
    public HashMap<String, Object> deleteBoard( @PathVariable("boardnum")int boardnum ){
        HashMap<String, Object>map = new HashMap<>();
        bs.deleteBoard(boardnum);
        map.put("msg", "OK");
        return map;
    }

    // 게시글 등록
    @PostMapping("/insertBoard")
    public HashMap<String, Object> insertBoard(@RequestBody Board board){
        HashMap<String, Object>map = new HashMap<>();
        bs.insertBoard(board);
        map.put("msg", "OK");
        return map;
    }

}
