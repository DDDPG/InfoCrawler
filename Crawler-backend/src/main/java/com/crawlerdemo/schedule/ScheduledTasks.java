package com.crawlerdemo.schedule;

import com.crawlerdemo.webmagic.CrawlerManager;
import com.crawlerdemo.webmagic.service.MailService;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

//    private final MailService mailService;
//
//    private final CrawlerManager crawlerManager;
//
//    public ScheduledTasks(MailService mailService, CrawlerManager crawlerManager) {
//        this.mailService = mailService;
//        this.crawlerManager = crawlerManager;
//    }
//
//
//    @Scheduled(cron = "0 0 11 * * ?", fixedDelay = 1000 * 60 * 5)
//    @Order(1)
//    public void doTaskNoon() {
//        this.crawlerManager.startCrawler();
//    }
//
//    @Scheduled(cron = "0 0 11 * * ?")
//    @Order(2)
//    public void doTask2Noon() {this.mailService.mail();}
//
//    @Scheduled(cron = "0 0 17 * * ?", fixedDelay = 1000 * 60 * 5)
//    @Order(3)
//    public void doTaskEven() {
//        this.crawlerManager.startCrawler();
//    }
//
//    @Scheduled(cron = "0 0 17 * * ?")
//    @Order(4)
//    public void doTask2Even() {this.mailService.mail();}
}