package com.crawlerdemo;

import com.crawlerdemo.webmagic.CrawlerManager;
import com.crawlerdemo.webmagic.service.MailService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * <p> This class is the main class of the project, and it is used to start the crawler.
 */
@SpringBootApplication
@MapperScan("com.crawlerdemo.webmagic.mapper")
//@EnableScheduling
public class WebCrawler {

    private final CrawlerManager crawlerManager;

    private final MailService mailService;


    public WebCrawler(CrawlerManager crawlerManager, MailService mailService) {
        this.crawlerManager = crawlerManager;
        this.mailService = mailService;
    }


    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebCrawler.class, args);
        WebCrawler webCrawler = context.getBean(WebCrawler.class);
//        webCrawler.crawlerManager.crawl("中国政府采购网_湖北省");
        webCrawler.mailService.mail();

    }
}
