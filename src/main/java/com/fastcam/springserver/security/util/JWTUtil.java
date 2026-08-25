package com.fastcam.springserver.security.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class JWTUtil {
    private static String key = "1234567890123456789012345678901234567890";

    public static String generateToken(Map<String, Object> claims, int min) {
        SecretKey key = null;
        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(claims)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))  // 현재시간
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))  // 유효시간
                .signWith(key)
                .compact();
        return jwtStr;
    }

    public static Map<String, Object> validateToken(String accessToken) throws JWTException {
        Map<String, Object> claim = null;
        SecretKey key = null;
        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch(ExpiredJwtException expiredJwtException){
            throw new JWTException("Expired");
        }catch(InvalidClaimException invalidClaimException){
            throw new JWTException("Invalid");
        }catch(JwtException jwtException){
            throw new JWTException("JWTError");
        }catch(Exception e){
            throw new JWTException("Error");
        }
        return claim;
    }

}
