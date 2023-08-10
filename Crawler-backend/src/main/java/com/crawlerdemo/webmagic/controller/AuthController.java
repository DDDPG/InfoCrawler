package com.crawlerdemo.webmagic.controller;

import com.crawlerdemo.security.context.AuthenticationContextHolder;
import com.crawlerdemo.security.model.LoginUser;
import com.crawlerdemo.security.service.TokenService;
import com.crawlerdemo.webmagic.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;
    @Resource
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/users/login")
    public LoginResponse login(String username, String password, String code) {
        System.out.println("username: " + username + " password: " + password + " code: " + code);
        int responseCode = loginPrecheck(username, password, code);

        if (responseCode != 0) {
            return new LoginResponse(responseCode, "前置校验失败", null);
        }

        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            return new LoginResponse(-4, "jwt认证失败", null);
        } finally {
            AuthenticationContextHolder.clearContext();
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String token = tokenService.createToken(loginUser);
        LoginResponse loginResponse = new LoginResponse(0, "登录成功", new LoginResponse.Data(token));
        return loginResponse;
    }

    @GetMapping(value = "/login/code")
    public CodeResponse getCode() {
        return new CodeResponse(0, "获取验证码成功", "https://via.placeholder.com/100x40/dcdfe6/000000.png?text=qidian");
    }

    @GetMapping(value = "/users/info")
    public UserResponse getInfo(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        String username = loginUser.getUsername();
        String[] roles = loginUser.getPermissions().toArray(String[]::new);
        return new UserResponse(0, "获取用户详情成功", new UserResponse.Data(username, roles));
    }

    private int loginPrecheck(String username, String password, String code) {
        if (username == null || password == null || code == null) {
            return -1;
        }
        if (!authService.authenticate(username, password)) {
            return -2;
        }
        if (!code.equals("qidian")) {
            return -3;
        }
        return 0;
    }


    @Data
    class UserResponse implements Serializable {
        private int code;
        private String message;
        private Data data;
        public UserResponse(int code, String message, Data data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        @lombok.Data
        static class Data implements Serializable{
            private String username;
            private String[] roles;

            public Data(String name, String[] roles) {
                this.username = name;
                this.roles = roles;
            }
        }

    }

    @lombok.Data
    class CodeResponse implements Serializable{
        private int code;
        private String message;
        private String data;

        public CodeResponse(int code, String message, String data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }
    }

    @lombok.Data
    class LoginResponse implements Serializable{
        private int code;
        private String message;
        private Data data;

        public LoginResponse(int code, String message, Data data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        @lombok.Data
        static
        class Data implements Serializable{
            private String token;

            public Data(String token) {
                this.token = token;
            }
        }
    }


}
