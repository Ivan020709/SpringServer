package com.fastcam.springserver.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTCheckFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        System.out.println("check uri................" + path);

        // 카카오 로그인 및 회원가입
        if(path.startsWith("/member/kakaostart"))
            return true;
        if(path.startsWith("/member/kakaoLogin"))
            return true;
        if(path.startsWith("/member/getLoginUser"))
            return true;
        if(path.startsWith("/member/updateKakaoMember"))
            return true;
        if(path.startsWith("/favicon.ico"))
            return true;

        // 아이디/비밀번호 찾기
        if(path.startsWith("/member/findId"))
            return true;
        if(path.startsWith("/member/findPwd"))
            return true;
        if(path.startsWith("/member/updatePwd"))
            return true;

        // 회원가입 및 로그인
        if(path.startsWith("/member/login"))
            return true;
        if(path.startsWith("/member/emailCheck"))
            return true;
        if(path.startsWith("/member/nicknameCheck"))
            return true;
        if(path.startsWith("/member/fileupload"))
            return true;
        if(path.startsWith("/member/insertMember"))
            return true;
        if(path.startsWith("/images"))
            return true;

        return false;
    }
}
