package com.worksmile.messageServer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable() // 기본값이 on 인 csrf 취약점 보안을 해제. on 으로 설정할경우 웹페이지에서 추가처리가 필요함
                .formLogin().disable()

                .headers()
                .frameOptions().sameOrigin() // SockJS는 기본적으로 HTML iframe 요소를 통한 전송을 허용하지 않도록 설정하여 해당 내용을 해제
                .and()

                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // PreFlightRequest 모두 허용
//                .antMatchers("/messages/**").hasRole("USER")
                .anyRequest().permitAll();
    }
}
