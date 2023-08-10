package com.crawlerdemo.webmagic.service;

import com.crawlerdemo.security.model.LoginUser;
import com.crawlerdemo.webmagic.Entity.security.AuthAccountEntity;
import com.crawlerdemo.security.config.SecurityConfig;
import com.crawlerdemo.webmagic.mapper.securitymapper.AuthAccountMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService implements UserDetailsService {

    @Resource
    private AuthAccountMapper authAccountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username == null) {
            throw new UsernameNotFoundException("User cannot be empty");
        }
        //find account entity by name
        MPJLambdaWrapper<AuthAccountEntity> wrapper = new MPJLambdaWrapper<AuthAccountEntity>()
                .eq(AuthAccountEntity::getUsername, username);
        AuthAccountEntity authAccountEntity = authAccountMapper.selectOne(wrapper);

        if (authAccountEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return createLoginUser(authAccountEntity);
    }

    private UserDetails createLoginUser(AuthAccountEntity authAccountEntity) {
        return new LoginUser((long) authAccountEntity.getId(), authAccountEntity, Collections.singleton(authAccountEntity.getAuthorities()));
    }

    public boolean authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        String passwordInDB = userDetails.getPassword();
        return SecurityConfig.passwordEncoder().matches(password, passwordInDB);
    }
}
