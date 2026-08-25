package com.fastcam.springserver.controller;

import com.fastcam.springserver.entity.AdminActivityLog;
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
            @RequestParam("reportnum") int reportnum,
            @RequestParam("adminid") String adminid,
            @RequestParam("adminname") String adminname
    ){
        HashMap<String, Object> map = new HashMap<>();
        as.deleteReport(reportnum, adminid, adminname);
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

    @GetMapping("/getAdminActivityLog")
    public List<AdminActivityLog> getAdminActivityLog() {

        List<AdminActivityLog> list = as.getAdminActivityLog();

        System.out.println("Controller 반환 데이터 : " + list);

        return list;
    }

    @GetMapping("/CheckError")
    public HashMap<String,Object>CheckError(@RequestParam("errornum") int errornum){
        HashMap<String, Object> map = new HashMap<>();
        as.checkerror(errornum);
        map.put("msg","OK");
        return map;
    }

    @PostMapping("/addRole")
    public HashMap<String,Object> addRole(
            @RequestParam("userid") int userid,
            @RequestParam("code") String code
    ) {

        System.out.println("===== addRole 호출 =====");
        System.out.println("userid : " + userid);
        System.out.println("code : " + code);

        HashMap<String, Object> map = new HashMap<>();

        boolean result = as.checkAdminCode(userid, code);

        if (result) {
            map.put("msg", "OK");
        } else {
            map.put("msg", "FAIL");
        }

        return map;
    }
    @GetMapping("/getAdmin")
    public HashMap<String, Object> getAdmin(@RequestParam("email") String email) {

        return as.getAdmin(email);
    }

    @PostMapping("/writeAnswer")
    public HashMap<String, Object> writeAnswer(
            @RequestParam("inquirynum")int inquirynum,
            @RequestParam("nickname") String nickname,
            @RequestParam("content")String content){
        HashMap<String, Object >map = new HashMap<>();
        as.insertAnswer(inquirynum, nickname, content);
        return map;
    }

    @GetMapping("/getAdminAnswer")
    public HashMap<String, Object>getAdminAnswer(
            @RequestParam("inquiryId")int inquiryId
    ){
        HashMap<String, Object>map = new HashMap<>();

        map.put("answers", as.getAdminAnswer(inquiryId));
        return map;
    }


}
