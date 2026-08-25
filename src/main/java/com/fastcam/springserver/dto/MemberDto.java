package com.fastcam.springserver.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemberDto extends User {
    // 생성자
    public MemberDto(
            int username,
            String password,
            String name,
            String nickname,
            String email,
            String phone,
            String birth,
            String savefilename,
            String zip_num,
            String address1,
            String address2,
            String address3,
            String provider,
            String snsid,
            String editcom,
            String role
    ) {
        // =========================================
        // Spring Security User 생성자
        // =========================================
        super(
                String.valueOf(username),
                password,
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + role)
                ) // User -> Role_user, Admin -> Role_Admin
        );


        // =========================================
        // 회원 정보 저장
        // =========================================

        this.userid = username;
        this.pwd = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.birth = birth;
        this.savefilename = savefilename;
        this.zip_num = zip_num;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.provider = provider;
        this.snsid = snsid;
        this.editcom = editcom;
        this.role = role;
    }


    private int userid;
    private String pwd;
    private String name;
    private String nickname;
    private String email;
    private String phone;
    private String birth;
    private String savefilename;
    private String zip_num;
    private String address1;
    private String address2;
    private String address3;
    private String provider;
    private String snsid;
    private String editcom = "N";
    private String role;


    // =========================================
    // JWT Claims
    // =========================================

    public Map<String, Object> getClaims() {

        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("userid", userid);
        dataMap.put("pwd", pwd);
        dataMap.put("name", name);
        dataMap.put("nickname", nickname);
        dataMap.put("email", email);
        dataMap.put("phone", phone);
        dataMap.put("birth", birth);
        dataMap.put("savefilename", savefilename);
        dataMap.put("zip_num", zip_num);
        dataMap.put("address1", address1);
        dataMap.put("address2", address2);
        dataMap.put("address3", address3);
        dataMap.put("provider", provider);
        dataMap.put("snsid", snsid);
        dataMap.put("editcom", editcom);
        dataMap.put("role", role);

        return dataMap;
    }
}