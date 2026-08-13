package com.fastcam.springserver.controller;

import com.fastcam.springserver.dto.KakaoProfile;
import com.fastcam.springserver.dto.OAuthToken;
import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.service.MemberService;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;


@RestController
@RequestMapping("/member")
public class MemberController {
    
    @Autowired
    MemberService ms;


    @PostMapping("/login")
    public HashMap<String, Object>login(
            @RequestParam("email") String email,
            @RequestParam("pwd") String pwd
    ){
        HashMap<String, Object> map = new HashMap<String, Object>();
        Member mdto = ms.getMemberByEmail(email);

        if(mdto ==null){
            map.put("msg","notOK" );
            return map;
        } else if(!mdto.getPwd().equals(pwd)){
            map.put("msg","notOK");
            return map;
        }else{
            map.put("msg", "OK");
            map.put("loginUser",mdto);
        }
        return map;
    }

//    @GetMapping("/getMember")
//    public HashMap<String, Object>getMember(@RequestParam("email") String email){
//        HashMap<String, Object> map = new HashMap<>();
//        ms.getMemberByEmail(email);
//
//    }

    @PostMapping("/insertMember")
    public HashMap<String, Object>join(@RequestBody Member member){
        HashMap<String, Object> map = new HashMap<>();
        ms.insertMember(member);
        map.put("msg", "OK");
        return map;

    }

    @PostMapping("/emailCheck")
    public HashMap<String, Object>emailCheck(@RequestParam ("email") String email){
        HashMap<String, Object>map = new HashMap<>();
        Member mdto  = ms.getMemberByEmail(email);
        if(mdto==null){
            map.put("msg", "OK");
        }else{
            map.put("msg","notOK");
        }
        return map;

    }

    @PostMapping("/nicknameCheck")
    public HashMap<String, Object> nicknamecheck( @RequestParam("nickname") String nickname){
        HashMap<String, Object> map = new HashMap<String, Object>();
        Member mdto = ms.getMemberByNickname(nickname);
        if( mdto == null )
            map.put("msg", "OK");
        else
            map.put("msg", "notOK");
        return map;
    }


    @Autowired
    ServletContext sc;

    @PostMapping("/fileupload")
    public HashMap<String, Object> fileupload(@RequestParam("image") MultipartFile file){
        HashMap<String, Object> map = new HashMap<String, Object>();
        String path = sc.getRealPath("/images");
        Calendar today = Calendar.getInstance();
        long dt = today.getTimeInMillis();
        String filename = file.getOriginalFilename();
        String f1 = filename.substring(0, filename.lastIndexOf("."));
        String f2 = filename.substring(filename.lastIndexOf("."));
        String uploadPath = path + "/" + f1 + dt + f2;
        try {
            file.transferTo( new File(uploadPath) );
            map.put("filename", f1 + dt + f2);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    @Value("${kakao.client_id}")
    private String client_id;
    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @GetMapping("/kakaostart")
    public @ResponseBody  String kakaostart(){
        String a = "<script type='text/javascript'>"
                + "location.href='https://kauth.kakao.com/oauth/authorize?"
                + "client_id=" + client_id + "&"
                + "redirect_uri=" + redirect_uri + "&"
                + "response_type=code';" + "</script>";
        return a;
    }

    @GetMapping("/kakaoLogin")
    public void kakaoLogin(HttpServletRequest request, HttpServletResponse response  ) throws IOException {

        String code = request.getParameter("code");

        String endpoint = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(endpoint);
        String bodyData = "grant_type=authorization_code&";
        bodyData += "client_id=" + client_id + "&";
        bodyData += "redirect_uri=" + redirect_uri + "&";
        bodyData += "code=" + code;

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        conn.setDoOutput(true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
        bw.write(bodyData);
        bw.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String input = "";
        StringBuilder sb = new StringBuilder();
        while ((input = br.readLine()) != null) {
            sb.append(input);
        }
        Gson gson = new Gson();
        OAuthToken oAuthToken = gson.fromJson(sb.toString(), OAuthToken.class);
        String endpoint2 = "https://kapi.kakao.com/v2/user/me";
        URL url2 = new URL(endpoint2);

        HttpsURLConnection conn2 = (HttpsURLConnection) url2.openConnection();
        conn2.setRequestProperty("Authorization", "Bearer " + oAuthToken.getAccess_token());
        conn2.setDoOutput(true);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream(), "UTF-8"));
        String input2 = "";
        StringBuilder sb2 = new StringBuilder();
        while ((input2 = br2.readLine()) != null) {
            sb2.append(input2);
            System.out.println(input2);
        }
        Gson gson2 = new Gson();
        KakaoProfile kakaoProfile = gson2.fromJson(sb2.toString(), KakaoProfile.class);
        KakaoProfile.KakaoAccount ac = kakaoProfile.getAccount();
        KakaoProfile.KakaoAccount.Profile pf = ac.getProfile();

        System.out.println("id : " + kakaoProfile.getId());
        System.out.println("Profile-Nickname : " + pf.getNickname());

        Member mdto = ms.getMemberBySnsid( kakaoProfile.getId() );
        if( mdto == null){
            mdto = new Member();
            mdto.setName( pf.getNickname() );
            mdto.setProvider( "KAKAO" );
            mdto.setSnsid(kakaoProfile.getId());
            ms.insertMember(mdto);
        }
        response.sendRedirect("http://localhost:3000/savekakaoinfo/" + mdto.getUserid() );
    }

    @PostMapping("/updateMember")
    public HashMap<String, Object> updateMember(@RequestBody Member member ){
        HashMap<String, Object> map = new HashMap<>();
        ms.updateMember(member);
        map.put("updateMember", member);
        return map;

    }



}
