package com.fastcam.springserver.security;

import com.fastcam.springserver.security.filter.JWTCheckFilter;
import com.fastcam.springserver.security.handler.APILoginFailHandler;
import com.fastcam.springserver.security.handler.APILoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Security Filter Chain - Security Config Start -----------------------");

        //cors
        http.cors(
                httpSecurityCorsConfigurer ->  {
                    httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
                }
        );

        //csrf - 토근 사용 0 & 세션 사용 X
        http.csrf(config -> config.disable());
        //세션도 쓰지 않음
        http.sessionManagement(
                sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 로그인 처리 설정
        http.formLogin(
                config -> {
                    config.loginPage("/member/login");
                    config.usernameParameter("userid");
                    config.passwordParameter("pwd");
                    config.successHandler(new APILoginSuccessHandler());
                    config.failureHandler(new APILoginFailHandler());
                }
        );

        // 토근에 관련한 검증
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        // 접근시 발생한 모든 예외 처리(엑세스 토큰 오류 , 로그인 오류 등등)에 대한 설정
        http.exceptionHandling(config -> {
            config.accessDeniedHandler(new com.fastcam.springserver.security.handler.AccessDeniedHandler());
        });

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 아이피
        configuration.setAllowedOriginPatterns( Arrays.asList("*") );
        // 메서드방식
        configuration.setAllowedMethods( Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE") );
        // 헤더
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        // 전송해줄 데이터의 JSON 처리
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
