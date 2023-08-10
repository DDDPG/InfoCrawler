package com.crawlerdemo;

import com.crawlerdemo.webmagic.CrawlerManager;
import com.crawlerdemo.webmagic.service.MailService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p> This class is the main class of the project, and it is used to start the crawlermapper.
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.crawlerdemo.webmagic.mapper.securitymapper", "com.crawlerdemo.webmagic.mapper.crawlermapper"})
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
//        webCrawler.mailService.mail();
//        PasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println(encoder.encode("123"));
    }
}
