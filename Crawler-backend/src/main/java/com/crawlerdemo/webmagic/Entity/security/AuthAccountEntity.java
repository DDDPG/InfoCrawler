package com.crawlerdemo.webmagic.Entity.security;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("authaccount")
public class AuthAccountEntity implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    int id;
    @TableField("username")
    private String username;
    @TableField("password")
    private String password;
    @TableField("authorities")
    private String authorities;
    @TableField("email")
    private String email;

    public AuthAccountEntity(String username, String password, String email, String authorities) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public AuthAccountEntity() {
    }

    public AuthAccountEntity(int id, String username, String password, String authorities, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.email = email;
    }
}
