package com.crawlerdemo.webmagic.Entity.crawler;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Entity class for the crawlermapper management info from table CrawlerManagementMap
 */
@Data
@TableName("CrawlerManagementMap")
public class CrawlerManagementEntity {

    @TableId(value = "ID", type = IdType.AUTO)
    private int id;

    @TableField("ifCrawl")
    private int ifCrawl;

    @TableField("websiteName")
    private String websiteName;

    @TableField("homePage")
    private String homePage;

    @TableField("contact")
    private String contact;

    @TableField("emails")
    private String emails;

    @TableField("crawlerKeywords")
    private String crawlerKeywords;

    public CrawlerManagementEntity() {
    }

    public CrawlerManagementEntity(int ifCrawl, String websiteName, String homePage, String contact, String emails, String crawlerKeywords) {
        this.ifCrawl = ifCrawl;
        this.websiteName = websiteName;
        this.homePage = homePage;
        this.contact = contact;
        this.emails = emails;
        this.crawlerKeywords = crawlerKeywords;
    }

    public CrawlerManagementEntity(int id, int ifCrawl, String websiteName, String homePage, String contact, String emails, String crawlerKeywords) {
        this.id = id;
        this.ifCrawl = ifCrawl;
        this.websiteName = websiteName;
        this.homePage = homePage;
        this.contact = contact;
        this.emails = emails;
        this.crawlerKeywords = crawlerKeywords;
    }
}
