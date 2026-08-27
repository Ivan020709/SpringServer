package com.fastcam.springserver.service;

import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.entity.MemberRole;
import com.fastcam.springserver.repository.MemberRepository;
import com.fastcam.springserver.repository.MemberRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class MemberService {

    @Autowired
    MemberRepository mr;

    @Autowired
    MemberRoleRepository mrr;

    BCryptPasswordEncoder pe = new BCryptPasswordEncoder();

    public void insertMember(Member member) {

        // 비밀번호 BCrypt 암호화
        member.setPwd(pe.encode(member.getPwd()));

        // 회원 저장
        mr.save(member);

        // 회원 정보 조회
        Member mainmember = mr.findByEmail(member.getEmail());

        // 권한 저장
        MemberRole memberrole = new MemberRole();
        memberrole.setEmail(mainmember.getEmail());
        memberrole.setRole("USER");

        mrr.save(memberrole);
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

    public Member updateKakaoMember(Member member, int userId) {
        Member oldMember = requireMember(userId);

        oldMember.setEmail(member.getEmail());
        oldMember.setNickname(member.getNickname());
        oldMember.setPhone(member.getPhone());
        oldMember.setZip_num(member.getZip_num());
        oldMember.setAddress1(member.getAddress1());
        oldMember.setAddress2(member.getAddress2());
        oldMember.setAddress3(member.getAddress3());
        oldMember.setEditcom("Y");

        System.out.println("수정 후 회원 = " + oldMember);
        return oldMember;
    }

    private Member requireMember(int userId) {
        Member member = mr.findByUserid(userId);
        if (member == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 회원을 찾을 수 없습니다.");
        }
        return member;
    }

    public void deleteMember(String email) {
        Member member = mr.findByEmail(email);
        mr.delete(member);
    }

    public Member findId(String name, String phone) {
        Member member = mr.findByNameAndPhone(name, phone);
        return member;
    }

    public Member findPwd(String email, String name, String phone) {
        Member member = mr.findByEmailAndNameAndPhone(email, name, phone);
        return member;
    }

    public Member updatePwd(String email, String password) {
        Member member = mr.findByEmail(email);

        if (member != null && password != null && !password.isBlank()) {
            // 새 비밀번호를 그대로 저장하지 않고 BCrypt로 암호화해서 저장합니다.
            member.setPwd(pe.encode(password));
            mr.save(member);
        }

        return member;
    }

    public void updateMember(Member member) {
        Member oldMember = mr.findByEmail(member.getEmail());

        // 새 비밀번호가 들어온 경우에만 변경합니다.
        // 빈 문자열이면 DB에 저장된 기존 암호화 비밀번호를 유지합니다.
        if (member.getPwd() != null && !member.getPwd().isBlank()) {
            oldMember.setPwd(pe.encode(member.getPwd()));
        }
        oldMember.setNickname(member.getNickname());
        oldMember.setPhone(member.getPhone());
        oldMember.setZip_num(member.getZip_num());
        oldMember.setAddress1(member.getAddress1());
        oldMember.setAddress2(member.getAddress2());
        oldMember.setAddress3(member.getAddress3());

        // 새로 업로드한 프로필 사진 파일명을 회원 정보에 저장m
        oldMember.setSavefilename(member.getSavefilename());
    }

    public void insertKakaoMember(Member mdto) {

        // 카카오 회원은 비밀번호가 없으므로 암호화하지 않음
        mr.save(mdto);

        // 회원 정보 조회
        Member mainmember = mr.findBySnsid(mdto.getSnsid());

        // 권한 저장
        MemberRole memberrole = new MemberRole();
        memberrole.setEmail(mainmember.getEmail());
        memberrole.setRole("USER");

        mrr.save(memberrole);
    }
}
