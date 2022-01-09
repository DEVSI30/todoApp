package com.example.todoapp.config;

import com.example.todoapp.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .httpBasic().disable() // token을 사용하므로 basic 인증 disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 기반이 아님
                .and()
                .authorizeHttpRequests() // /와 /auth/** 경로는 인증 안해도 됨
                .antMatchers("/", "/auth/**").permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class // Cors 필터 다음에 실행해라
        );
    }
}
