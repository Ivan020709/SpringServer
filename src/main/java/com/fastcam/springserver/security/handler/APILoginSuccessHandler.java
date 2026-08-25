package com.fastcam.springserver.security.handler;

import com.fastcam.springserver.dto.MemberDto;
import com.fastcam.springserver.security.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //authentication 매개변수에 전달된 MemberDto를 꺼내고
        MemberDto memberDto = (MemberDto) authentication.getPrincipal();

        //로그인에 성공한 정보들로 토큰을 제작
        //claims 추출
        Map<String, Object> claims = memberDto.getClaims();

        //토큰 생성
        String accessToken = JWTUtil.generateToken(claims, 1);
        String refreshToken = JWTUtil.generateToken(claims, 60*24);

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        //클라이언트로 전송
        Gson gson = new Gson();
        String jsonStr = gson.toJson(claims);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();
    }
}
