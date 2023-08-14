package com.crawlerdemo.webmagic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crawlerdemo.webmagic.Entity.crawler.ResultInfoEntity;
import com.crawlerdemo.webmagic.Entity.security.AuthAccountEntity;
import com.crawlerdemo.webmagic.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * This class is used to operate the user information table from `auth_account` in vue view and `AuthAccountMap` in database.
 */
@RestController
@RequestMapping("/api/v1/userTable")
public class UserTableOperationController {

    @Autowired
    private UserService userService;

    /**
     * 插入用户信息表
     * @param username: 用户名
     * @param password: 密码
     * @param email: 邮箱
     * @param authorities: 权限
     * @return NormalSQLResponse: 插入用户信息表的响应体
     */
    @PostMapping
    public NormalSQLResponse insertUserTable(String username,
                                             String password,
                                             String email,
                                             String authorities) {
        //Encode the password to the BCrypt format
        String encodedPassword = new BCryptPasswordEncoder().encode(password);

        AuthAccountEntity authAccountEntity = new AuthAccountEntity(username, encodedPassword, email, authorities);

        int result = userService.insertUserTable(authAccountEntity);

        NormalSQLResponse normalSQLResponse = new NormalSQLResponse();
        if (result == 1) {
            normalSQLResponse.setCode(0);
            normalSQLResponse.setMessage("插入成功");
        } else {
            normalSQLResponse.setCode(1);
            normalSQLResponse.setMessage("插入失败");
        }
        return normalSQLResponse;
    }

    /**
     * 删除用户信息表
     * @param id: 要删除的用户信息表的id
     * @return NormalSQLResponse: 删除用户信息表的响应体
     */
    @DeleteMapping("/{id}")
    public NormalSQLResponse deleteUserTable(@PathVariable int id) {
        int result = userService.deleteUserTable(id);

        NormalSQLResponse normalSQLResponse = new NormalSQLResponse();
        if (result == 1) {
            normalSQLResponse.setCode(0);
            normalSQLResponse.setMessage("删除成功");
        } else {
            normalSQLResponse.setCode(1);
            normalSQLResponse.setMessage("删除失败");
        }
        return normalSQLResponse;
    }

    /**
     * 更新用户信息表
     * @param id: 要更新的用户信息表的id
     * @param username: 用户名
     * @param password: 密码
     * @param email: 邮箱
     * @param authorities: 权限
     * @return NormalSQLResponse: 更新用户信息表的响应体
     */
    @PutMapping
    public NormalSQLResponse updateUserTable(Integer id,
                                             String username,
                                             String password,
                                             String email,
                                             String authorities) {
        AuthAccountEntity authAccountEntity = new AuthAccountEntity(id, username, password, email, authorities);

        int result = userService.updateUserTable(authAccountEntity);

        NormalSQLResponse normalSQLResponse = new NormalSQLResponse();
        if (result == 1) {
            normalSQLResponse.setCode(0);
            normalSQLResponse.setMessage("更新成功");
        } else {
            normalSQLResponse.setCode(1);
            normalSQLResponse.setMessage("更新失败");
        }
        return normalSQLResponse;
    }

    /**
     * 查询用户信息表
     * @param page: 页数
     * @param pageSize: 每页的大小
     * @param username: 用户名
     * @param email: 邮箱
     * @param authorities: 权限
     * @return SelectSQLResponse: 查询用户信息表的响应体
     */
    @GetMapping
    public SelectSQLResponse selectUserTable(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                             String username,
                                             String email,
                                             String authorities) {
        Page<AuthAccountEntity> authAccountEntityPage = userService.selectUserTable(page, pageSize, username, email, authorities);
        List<AuthAccountEntity> authAccountEntityList = authAccountEntityPage.getRecords();
        long total = authAccountEntityPage.getTotal();

        SelectSQLResponse selectSQLResponse = new SelectSQLResponse();
        selectSQLResponse.setCode(0);
        selectSQLResponse.setMessage("查询成功");
        selectSQLResponse.setData(new SelectSQLResponse.Data(authAccountEntityList, total));
        return selectSQLResponse;
    }
    @Data
    static class NormalSQLResponse implements Serializable {
        private int code;
        private String message;
        private Data data;

        public NormalSQLResponse() {
        }

        public NormalSQLResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }
        static class Data implements Serializable {
            public Data() {
            }
        }
    }

    @Data
    static class SelectSQLResponse implements Serializable{
        private int code;
        private String message;
        private Data data;

        public SelectSQLResponse() {
        }

        public SelectSQLResponse(int code, String message, Data data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        @lombok.Data
        static
        class Data implements Serializable {
            private List<AuthAccountEntity> list;
            private long total;

            public Data() {
            }

            public Data(List<AuthAccountEntity> resultInfoEntityList, long total) {
                this.list = resultInfoEntityList;
                this.total = total;
            }
        }
    }

}
