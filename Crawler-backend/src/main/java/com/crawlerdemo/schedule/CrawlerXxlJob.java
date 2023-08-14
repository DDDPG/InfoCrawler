package com.crawlerdemo.schedule;

import com.crawlerdemo.webmagic.CrawlerManager;
import com.crawlerdemo.webmagic.service.MailService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrawlerXxlJob {
    @Autowired
    CrawlerManager crawlerManager;

    @Autowired
    MailService mailService;

    @XxlJob("PushJobHandler")
    public void TestJob(String param) {
        XxlJobHelper.log("The job is running with param: " + param);
    }

    @XxlJob("CrawlerJobHandler")
    public ReturnT<String> CrawlerJobHandler(String param) {
        try {
            crawlerManager.startCrawler();
        } catch (Exception e) {
            XxlJobHelper.log("Crawler task failed: " + e.getMessage());
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    @XxlJob("InfoMailJobHandler")
    public ReturnT<String> InfoMailJobHandler(String param) {
        try {
            mailService.mail();
        } catch (Exception e) {
            XxlJobHelper.log("Mail sending task failed: " + e.getMessage());
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
