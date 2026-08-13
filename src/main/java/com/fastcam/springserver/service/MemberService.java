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

    public Member getMemberByEmail(String email) {
        return mr.findByEmail(email);
    }

    public Member getMemberByNickname(String nickname) {
        return mr.findByNickname(nickname);
    }


    public Member getMemberBySnsid(String id) {
        return mr.findBySnsid(id);
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
}
