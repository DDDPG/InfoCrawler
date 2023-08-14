package com.crawlerdemo.security.config;


import com.alibaba.fastjson.JSONObject;
import com.crawlerdemo.security.filter.JwtAuthenticationTokenFilter;
import com.crawlerdemo.webmagic.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/**", "/users/login", "/users/register").permitAll() //pass api
                                .anyRequest().authenticated()//other request need authentication
                )
                .cors(cors ->
                        cors.configurationSource(request -> {
                            var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                            corsConfiguration.addAllowedOrigin("http://localhost:3333");
                            corsConfiguration.addAllowedOrigin("http://192.168.0.85:3333");
                            corsConfiguration.addAllowedOrigin("http://192.168.0.85:6066");
                            corsConfiguration.addAllowedHeader("*");
                            corsConfiguration.addAllowedMethod("*");
                            return corsConfiguration;
                        }))
                .userDetailsService(authService)
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig
                .getAuthenticationManager();
    }

//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(JSONObject.toJSONString("登陆成功" + authentication.getPrincipal()));
//    }
//
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                                        AuthenticationException exception) throws IOException {
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(JSONObject.toJSONString("登陆失败" + exception.getMessage()));
//    }

}
