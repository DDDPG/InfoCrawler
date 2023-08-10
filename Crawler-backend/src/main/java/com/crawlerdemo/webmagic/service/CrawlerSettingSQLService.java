package com.crawlerdemo.webmagic.service;

import com.crawlerdemo.webmagic.Entity.crawler.CrawlerSettingEntity;
import com.crawlerdemo.webmagic.mapper.crawlermapper.CrawlerSettingMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class CrawlerSettingSQLService {

    @Resource
    private CrawlerSettingMapper crawlerSettingMapper;

    public CrawlerSettingEntity getAllSettingsAsMap() {
        //Take the active setting (status = 1) from the database
        MPJLambdaWrapper<CrawlerSettingEntity> wrapper = new MPJLambdaWrapper<CrawlerSettingEntity>()
                .eq("status", 1);
        return this.crawlerSettingMapper.selectOne(wrapper);
    }
}
