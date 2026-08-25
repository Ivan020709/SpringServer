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

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        System.out.println("check uri................" + path);

        if(path.startsWith("/member/emailCheck"))
            return true;

        if(path.startsWith("/member/nicknameCheck"))
            return true;

        if(path.startsWith("/member/fileupload"))
            return true;

        if(path.startsWith("/member/insertMember"))
            return true;

        // 이미지 파일은 JWT 검사 제외
        if(path.startsWith("/images"))
            return true;

        return false;
    }
}
