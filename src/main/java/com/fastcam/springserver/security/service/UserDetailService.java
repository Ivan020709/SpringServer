package com.fastcam.springserver.security.service;

import com.fastcam.springserver.dto.MemberDto;
import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.entity.MemberRole;
import com.fastcam.springserver.repository.MemberRepository;
import com.fastcam.springserver.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final MemberRepository mr;
    private final MemberRoleRepository mrr;


    // =====================================================
    // 로그인 사용자 조회
    // =====================================================

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        System.out.println(
                "loadUserByUsername call - username : "
                        + username
        );


        // =================================================
        // userid가 int이므로 String → int 변환
        // =================================================

        int userid;

        try {
            userid = Integer.parseInt(username);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException(
                    username + " - 잘못된 사용자 ID"
            );

        }


        // =================================================
        // 회원 조회
        // =================================================

        Member member = mr.findByUserid(userid);

        if (member == null) {
            throw new UsernameNotFoundException(
                    username + " - User Not Found"
            );

        }


        // =================================================
        // 회원 권한 조회
        // MemberRole은 하나의 role만 사용
        // =================================================

        MemberRole memberRole =
                mrr.findByEmail(member.getEmail());


        String role = "USER";


        if (memberRole != null
                && memberRole.getRole() != null
                && !memberRole.getRole().isBlank()) {
            role = memberRole.getRole();

        }


        System.out.println(
                "회원 : " + member.getNickname()
                        + " / 권한 : " + role
        );


        // =================================================
        // MemberDto 생성
        // =================================================

        MemberDto mdto = new MemberDto(
                member.getUserid(),
                member.getPwd(),
                member.getName(),
                member.getNickname(),
                member.getEmail(),
                member.getPhone(),
                member.getBirth(),
                member.getSavefilename(),
                member.getZip_num(),
                member.getAddress1(),
                member.getAddress2(),
                member.getAddress3(),
                member.getProvider(),
                member.getSnsid(),
                member.getEditcom(),
                role
        );


        return mdto;
    }
}