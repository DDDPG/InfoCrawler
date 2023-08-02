package com.crawlerdemo.webmagic.service;

import com.crawlerdemo.webmagic.Entity.ResultInfoEntity;
import com.crawlerdemo.webmagic.mapper.ResultInfoMapper;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Service
@Data
@EnableCaching
public class ResultSQLService {

    @Resource
    private ResultInfoMapper resultInfoMapper;

    @Cacheable(cacheNames = "resultInfo", key = "#resultInfoEntity != null ? #resultInfoEntity : 'defaultKey'", unless = "#result == null")
    public int saveToDatabase(ResultInfoEntity resultInfoEntity) {
        return resultInfoMapper.filterAndInsert(resultInfoEntity);
    }

}
