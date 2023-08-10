package com.crawlerdemo.webmagic.Entity.crawler;

import lombok.Data;

/**
 * Entity class for the mail sending service
 */
@Data
public class MailUnitInfoEntity {

    private String url;

    private String date;

    private String area;

    private String sourceWebsite;

    private String title;

    private String contact;

    private String emails;

    private String crawlerKeywords;
}
