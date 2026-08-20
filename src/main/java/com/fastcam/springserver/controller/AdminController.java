package com.fastcam.springserver.controller;

import com.fastcam.springserver.entity.AdminReport;
import com.fastcam.springserver.entity.Board;
import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService as;

    @PostMapping("/report")
    public HashMap<String, Object> report(
            @RequestBody AdminReport areport
    ) {
        HashMap<String, Object> map = new HashMap<>();

        as.getReport(areport);

        map.put("msg", "OK");
        return map;
    }

    @GetMapping("/getReportList")
    public HashMap<String, Object> getReportList(
            @RequestParam(value="page",required = false, defaultValue="1") int page){
        HashMap<String, Object> map = as.getReportList(page);
        return map;
    }


}
