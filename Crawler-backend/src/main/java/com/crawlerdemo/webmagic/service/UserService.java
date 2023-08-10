package com.crawlerdemo.webmagic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crawlerdemo.utils.StringUtils;
import com.crawlerdemo.webmagic.Entity.security.AuthAccountEntity;
import com.crawlerdemo.webmagic.mapper.securitymapper.AuthAccountMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Resource
    private AuthAccountMapper authAccountMapper;

    public int insertUserTable(AuthAccountEntity authAccountEntity) {
        return authAccountMapper.insert(authAccountEntity);
    }

    public int deleteUserTable(Integer id) {
        return authAccountMapper.deleteById(id);
    }

    public int updateUserTable(AuthAccountEntity authAccountEntity) {
        return authAccountMapper.updateById(authAccountEntity);
    }

    public Page<AuthAccountEntity> selectUserTable(Integer page, Integer pageSize, String username, String email, String authorities) {
        Page<AuthAccountEntity> authAccountEntityPage = new Page<>(page, pageSize);
        MPJLambdaWrapper<AuthAccountEntity> mpjLambdaWrapper = new MPJLambdaWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            mpjLambdaWrapper.like(AuthAccountEntity::getUsername, username);
        }
        if (StringUtils.isNotBlank(email)) {
            mpjLambdaWrapper.apply("FIND_IN_SET({0},email)", email);
        }
        if (StringUtils.isNotBlank(authorities)) {
            mpjLambdaWrapper.apply("FIND_IN_SET({0},authorities)", authorities);
        }
        return authAccountMapper.selectPage(authAccountEntityPage, mpjLambdaWrapper);
    }
}
