package com.crawlerdemo.webmagic;

import com.crawlerdemo.schedule.ScheduledTasks;
import com.crawlerdemo.webmagic.config.CrawlerManagementConfig;
import com.crawlerdemo.webmagic.config.CrawlerSettingConfig;
import com.crawlerdemo.webmagic.model.bibenet.BiBinetRepo;
import com.crawlerdemo.webmagic.model.ccgp_hubei.HuBeiGovRepo;
import com.crawlerdemo.webmagic.model.ggzy.gov.GlobalGovPageRepo;
import com.crawlerdemo.webmagic.model.zhiliaobiaoxun.ZhiLiaoRepo;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.util.Optional;

/**
 * The manager class of each crawler, which is used to start the crawler.
 */
@Data
@Component
public class CrawlerManager {

    @Qualifier("ResultInfoSQLPipeline")
    @Autowired
    private PageModelPipeline companyInfoSQLPipeline;

    @Autowired
    private CrawlerManagementConfig crawlerManagementConfig;

    @Autowired
    private CrawlerSettingConfig crawlerSettingConfig;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ScheduledTasks.class);

    private Site globalSite;

    private int threadNum;

    @PostConstruct
    public void init() {
        this.globalSite = Site.me()
                .setSleepTime(Optional.ofNullable(crawlerSettingConfig.getCrawlerSettingEntity().getSleepTime()).orElse(1000))
                .setRetryTimes(Optional.ofNullable(crawlerSettingConfig.getCrawlerSettingEntity().getMaxRetryTimes()).orElse(3))
                .setTimeOut(Optional.ofNullable(crawlerSettingConfig.getCrawlerSettingEntity().getTimeout()).orElse(3000));
        this.threadNum = Optional.ofNullable(crawlerSettingConfig.getCrawlerSettingEntity().getThreadNum()).orElse(5);
    }

    public void crawl(String keyword) {
        switch (keyword) {
            case "比比网" -> crawlBiBiData();
            case "中国政府采购网_湖北省" -> crawlHuBeiData();
            case "政府采购网" -> crawlGovData();
            case "知了标讯" -> crawlZhiLiao();
            default -> System.out.println("No such website name for crawler!");
        }
    }

    private void crawlBiBiData() {
        OOSpider.create(globalSite
                        , companyInfoSQLPipeline, BiBinetRepo.class)
                .addUrl(BiBinetRepo.startUrl)
                .thread(threadNum).run();
    }

    private void crawlHuBeiData() {
        OOSpider.create(globalSite
                        , companyInfoSQLPipeline, HuBeiGovRepo.class)
                .addUrl(HuBeiGovRepo.startUrl)
                .thread(threadNum).run();
    }

    private void crawlGovData() {
        OOSpider.create(globalSite
                        , companyInfoSQLPipeline, GlobalGovPageRepo.class)
                .addUrl(GlobalGovPageRepo.startUrl)
                .thread(threadNum).run();
    }

    public void crawlZhiLiao() {
        //TODO: The Amount of data is too big, careful to crawl.
        OOSpider.create(globalSite
                        , companyInfoSQLPipeline, ZhiLiaoRepo.class)
                .addUrl(ZhiLiaoRepo.startUrl)
                .thread(threadNum).run();
    }

    public void startCrawler() {
        logger.info("Crawling started!");

        //According to the website name list in the configuration file, start the crawler.
        for (String website: crawlerManagementConfig.getWebsiteNameList()) {
            crawl(website);
            logger.info("Crawling " + website + " finished!");
        }

        System.out.println("Crawling finished!");
    }
}
