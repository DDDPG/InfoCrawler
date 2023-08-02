package com.crawlerdemo.webmagic.config;

import com.crawlerdemo.webmagic.Entity.CrawlerSettingEntity;
import com.crawlerdemo.webmagic.service.CrawlerSettingSQLService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Data
@Scope("singleton")
@Configuration
public class CrawlerSettingConfig {

    @Autowired
    private CrawlerSettingSQLService crawlerSettingSQLService;

    private CrawlerSettingEntity crawlerSettingEntity;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CrawlerSettingConfig.class);

    @PostConstruct
    public void init() {
        this.crawlerSettingEntity = this.crawlerSettingSQLService.getAllSettingsAsMap();
        logger.warn("Current active crawler setting: " + this.crawlerSettingEntity.toString());
    }
}
