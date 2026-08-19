package com.fastcam.springserver.service;

import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    @Autowired
    MemberRepository mr;

    public void insertMember(Member member) {
        mr.save(member);
    }



    public void updateMember(Member member) {
        Member oldMember = mr.findByEmail(member.getEmail());

        oldMember.setPwd(member.getPwd());
        oldMember.setNickname(member.getNickname());
        oldMember.setPhone(member.getPhone());
        oldMember.setZip_num(member.getZip_num());
        oldMember.setAddress1(member.getAddress1());
        oldMember.setAddress2(member.getAddress2());
        oldMember.setAddress3(member.getAddress3());


    }

    public Member getEmail(String email) {
        return mr.findByEmail(email);
    }



    public Member getNickname(String nickname) {
        return mr.findByNickname(nickname);
    }

    public Member getSnsid(String id) {
        return mr.findBySnsid(id);
    }

    public Member getMemberBySnsid(String id) {
        Member member = mr.findBySnsid(id);
        return member;
    }



    public Member getMemberByUserid(int userid) {
        Member member = mr.findByUserid(userid);
        return member;
    }

    public void updateKakaoMember(Member member) {
        Member oldMember = mr.findByUserid(member.getUserid());

        oldMember.setEmail(member.getEmail());
        oldMember.setNickname(member.getNickname());
        oldMember.setPhone(member.getPhone());
        oldMember.setZip_num(member.getZip_num());
        oldMember.setAddress1(member.getAddress1());
        oldMember.setAddress2(member.getAddress2());
        oldMember.setAddress3(member.getAddress3());
        oldMember.setEditcom("Y");

        System.out.println("수정 후 회원 = " + oldMember);
    }
}
