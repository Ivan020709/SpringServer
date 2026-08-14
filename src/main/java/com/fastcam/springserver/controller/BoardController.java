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
    public HashMap<String, Object> getBoardList(@PathVariable("page") int page){
        HashMap<String, Object> map = bs.getBoardList(page);
        return map;
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
