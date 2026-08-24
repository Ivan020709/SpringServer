package com.fastcam.springserver.controller;

import com.fastcam.springserver.entity.NoticeBoard;
import com.fastcam.springserver.service.NoticeBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/notice")
public class NoticeBoardController {

    @Autowired
    NoticeBoardService nbs;

    @GetMapping("/getNoticeList")
    public HashMap<String, Object> getNoticeList (
            @RequestParam(value="page",required = false, defaultValue="1") int page,
            @RequestParam(value="key", required = false, defaultValue = "") String key,
            @RequestParam(value="searchType", required = false, defaultValue = "") String searchType){
        HashMap<String, Object> map = nbs.getBoardList(page, key, searchType);
        return map;
    }

    @PostMapping("/insertNotice")
    public HashMap<String, Object>insertNotice(@RequestBody NoticeBoard nboard){
        HashMap<String, Object> map = new HashMap<>();
        nbs.insertBoard(nboard);
        map.put("msg", "OK");
        return map;
    }

    @GetMapping("/getNotice/{noticenum}")
    public HashMap<String, Object> getNotice(
            @PathVariable int noticenum
    ) {
        HashMap<String, Object> map =
                new HashMap<>();

        NoticeBoard notice =
                nbs.getNotice(noticenum);

        if (notice == null) {
            map.put("msg", "FAIL");
        } else {
            map.put("msg", "OK");
            map.put("notice", notice);
        }

        return map;
    }

    @PostMapping("/plusCount")
    public HashMap<String, Object> plusCount(
            @RequestParam("boardnum") int boardnum
    ) {
        HashMap<String, Object> map =
                new HashMap<>();

        nbs.plusCount(boardnum);

        map.put("msg", "OK");

        return map;
    }
}
