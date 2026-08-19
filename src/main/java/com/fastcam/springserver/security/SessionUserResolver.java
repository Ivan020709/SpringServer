package com.fastcam.springserver.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SessionUserResolver {
    public int requireUserId(HttpSession session) {
        Integer userId = optionalUserId(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return userId;
    }

    public Integer optionalUserId(HttpSession session) {
        Object value = session.getAttribute("loginUserId");
        if (value instanceof Number number) {
            return number.intValue();
        }
        return null;
    }
}
