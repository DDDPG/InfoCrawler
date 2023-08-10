package com.crawlerdemo.webmagic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crawlerdemo.utils.StringUtils;
import com.crawlerdemo.webmagic.Entity.crawler.CrawlerManagementEntity;
import com.crawlerdemo.webmagic.mapper.crawlermapper.CrawlerManagementMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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

    @Transactional
    public int deleteManagementTable(Integer id) {
        return this.crawlerManagementMapper.deleteById(id);
    }

    public Page<CrawlerManagementEntity> selectManagementTable(Integer currentPage, Integer size, Boolean ifCrawl, String websiteName, String homePage, String contact, String emails, String crawlerKeywords) {
        Page<CrawlerManagementEntity> page = new Page<>(currentPage, size);

        MPJLambdaWrapper<CrawlerManagementEntity> lambdaWrapper = new MPJLambdaWrapper<CrawlerManagementEntity>();

        if (!Objects.isNull(ifCrawl)) {
            lambdaWrapper.eq(CrawlerManagementEntity::getIfCrawl, ifCrawl);
        }

        if (StringUtils.isNotBlank(websiteName)) {
            lambdaWrapper.like(CrawlerManagementEntity::getWebsiteName, websiteName);
        }

        if (StringUtils.isNotBlank(homePage)) {
            lambdaWrapper.like(CrawlerManagementEntity::getHomePage, homePage);
        }

        if (StringUtils.isNotBlank(contact)) {
            lambdaWrapper.apply("FIND_IN_SET({0}, contact)", contact);
        }

        if (StringUtils.isNotBlank(emails)) {
            lambdaWrapper.apply("FIND_IN_SET({0}, emails)", emails);
        }

        if (StringUtils.isNotBlank(crawlerKeywords)) {
            lambdaWrapper.apply("FIND_IN_SET({0}, crawlerKeywords)", crawlerKeywords);
        }

        return this.crawlerManagementMapper.selectPage(page, lambdaWrapper);
    }

    public int addManagementTable(CrawlerManagementEntity crawlerManagementEntity) {
        return this.crawlerManagementMapper.insert(crawlerManagementEntity);
    }

    public int updateManagementTable(CrawlerManagementEntity crawlerManagementEntity) {
        return this.crawlerManagementMapper.updateById(crawlerManagementEntity);
    }
}
