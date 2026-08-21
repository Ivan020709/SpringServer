package com.fastcam.springserver.controller;

import com.fastcam.springserver.entity.AdminError;
import com.fastcam.springserver.entity.AdminReport;
import com.fastcam.springserver.service.AdminErrorService;
import com.fastcam.springserver.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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

    @DeleteMapping("/deleteReport")
    public HashMap<String, Object> deleteReport(
            @RequestParam("reportnum") int reportnum
    ){
        HashMap<String, Object> map = new HashMap<>();
        as.deleteReport(reportnum);
        map.put("msg", "OK");
        return map;
    }

    @Autowired
    AdminErrorService aes;


    @PostMapping("/errorLog")
    public HashMap<String, Object> errorLog(
            @RequestBody AdminError error
    ) {

        HashMap<String, Object> map = new HashMap<>();

        error.setTime(LocalDateTime.now());
        error.setStatusCode(400);
        aes.saveError(error);

        map.put("msg", "OK");

        return map;
    }

    @GetMapping("/getErrorList")
    public List<AdminError> getErrorList() {

        return aes.getErrorList();

    }



}
