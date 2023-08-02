package com.crawlerdemo.webmagic.service;

import com.crawlerdemo.webmagic.Entity.CrawlerManagementEntity;
import com.crawlerdemo.webmagic.mapper.CrawlerManagementMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
@EnableCaching
public class ManagementSQLService {

    @Resource
    private CrawlerManagementMapper crawlerManagementMapper;

    @Cacheable(cacheNames = "crawlerManagement", key = "'defaultKey'", unless = "#result == null")
    public List<CrawlerManagementEntity> selectNeedToCrawl() {
        MPJLambdaWrapper<CrawlerManagementEntity> lambdaWrapper = new MPJLambdaWrapper<CrawlerManagementEntity>()
                .eq(CrawlerManagementEntity::getIfCrawl, 1);
        return this.crawlerManagementMapper.selectList(lambdaWrapper);
    }
}
