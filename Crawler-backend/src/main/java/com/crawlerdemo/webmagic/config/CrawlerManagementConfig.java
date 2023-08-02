package com.crawlerdemo.webmagic.config;

import com.crawlerdemo.webmagic.Entity.CrawlerManagementEntity;
import com.crawlerdemo.webmagic.model.ResultInfoRepo;
import com.crawlerdemo.webmagic.service.ManagementSQLService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to manage the crawler management entity from table "crawlermanagementmap".
 */
@Data
@Scope("singleton")
@Configuration
public class CrawlerManagementConfig {

    @Autowired
    private ManagementSQLService managementSQLService;

    private Map<String, CrawlerManagementEntity> crawlerManagementEntityMap = new HashMap<>();

    private List<String> websiteNameList = new ArrayList<>();

    @PostConstruct
    public void initMap() {
        //Get the website of which ifCrawl is true
        List<CrawlerManagementEntity> crawlerManagementEntityList = this.managementSQLService.selectNeedToCrawl();

        //Put the data into the map (key: websiteName, value: CrawlerManagementEntity) and list (value: websiteName)
        for (CrawlerManagementEntity crawlerManagementEntity : crawlerManagementEntityList) {
            this.websiteNameList.add(crawlerManagementEntity.getWebsiteName());
            this.crawlerManagementEntityMap.put(crawlerManagementEntity.getWebsiteName(), crawlerManagementEntity);
        }
    }

    public CrawlerManagementEntity getManagementEntity(ResultInfoRepo resultInfoRepo) {
        // Get the CrawlerManagementEntity by websiteName(type)
        CrawlerManagementEntity crawlerManagementEntity = this.crawlerManagementEntityMap.get(resultInfoRepo.getSourceWebsite());
        if (crawlerManagementEntity == null) {
            throw new RuntimeException("No CrawlerManagementEntity for websiteName: " + resultInfoRepo.getSourceWebsite());
        }
        return crawlerManagementEntity;
    }

}
