package com.fastcam.springserver.service;

import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.entity.MemberRole;
import com.fastcam.springserver.repository.MemberRepository;
import com.fastcam.springserver.repository.MemberRoleRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
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

    // 이메일 전송객체
    private final JavaMailSender JMSender;

    // 이메일 전송주체
    @Value("${spring.mail.username}")
    private String senderEmail;

    public int sendMail(String email) {

        // 메일 한 건마다 별도의 인증번호를 생성합니다.
        int number = (int)(Math.random() * (90000)) + 100000;

        // 수신 이메일, 제목 내용 등등을 설정할 객체를 생성
        // 전송될 이메일 내용(수신자, 제목, 내용 등) 구성 객체
        MimeMessage message = JMSender.createMimeMessage();

        try {
            message.setFrom( senderEmail );  // 보내는 사람 설정
            message.setRecipients( MimeMessage.RecipientType.TO, email );  // 받는 사람 설정
            message.setSubject("이메일 인증");  // 제목 설정
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");  // 본문 설정
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        JMSender.send(message);  // 구성 완료된 message 를 JMSender 로 전송
        return number;
    }
}
