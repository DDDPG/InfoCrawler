package com.crawlerdemo.webmagic.service;

import com.crawlerdemo.mail.MailPool;
import com.crawlerdemo.webmagic.Entity.crawler.CrawlerManagementEntity;
import com.crawlerdemo.webmagic.Entity.crawler.MailUnitInfoEntity;
import com.crawlerdemo.webmagic.Entity.crawler.ResultInfoEntity;
import com.crawlerdemo.webmagic.mapper.crawlermapper.ResultInfoMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import lombok.Data;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Service

public class MailService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MailService.class);

    private final MailPool mailPool;

    @Resource
    private ResultInfoMapper resultInfoJoinMapper;


    @Value("${spring.mail.username}")
    private String from;

    public MailService(MailPool mailPool) {
        this.mailPool = mailPool;
    }

    public List<MailUnitInfoEntity> getRecentMails() {
        //Get the changed infos in newest 3 hour (Today refreshed)
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(3);
        logger.info("Get mails updated later than: {}", oneHourAgo);
        Timestamp timestamp = Timestamp.from(oneHourAgo.toInstant(ZoneOffset.of("+8")));

        //Generate wrapper (All columns in ResultInfoMap && Contact, Emails, CrawlerKeywords in CrawlerManagementMap)
        MPJLambdaWrapper<ResultInfoEntity> lambdaWrapper = new MPJLambdaWrapper<ResultInfoEntity>()
                .gt("t.updateTime", timestamp)
                .selectAll(ResultInfoEntity.class)
                .select(CrawlerManagementEntity::getContact, CrawlerManagementEntity::getEmails, CrawlerManagementEntity::getCrawlerKeywords)
                .leftJoin(CrawlerManagementEntity.class, CrawlerManagementEntity::getWebsiteName, ResultInfoEntity::getSourceWebsite);

        return resultInfoJoinMapper.selectJoinList(MailUnitInfoEntity.class, lambdaWrapper);
    }

    public void saveToMailPool(List<MailUnitInfoEntity> mailUnitInfoEntities) {
        mailPool.getMailUnitInfoEntities().addAll(mailUnitInfoEntities);
        mailPool.collectMails();
    }

    public void sendMail() {
        mailPool.sendMails();
        mailPool.clear();
    }

    public void mail() {
        List<MailUnitInfoEntity> mailUnitInfoEntityList = getRecentMails();
        CopyOnWriteArrayList<MailUnitInfoEntity> mailUnitInfoEntities = new CopyOnWriteArrayList<>(mailUnitInfoEntityList);
        if (mailUnitInfoEntities.isEmpty()) {
            System.out.println("No new info!");
            return;
        }
        for (MailUnitInfoEntity mailUnitInfoEntity : mailUnitInfoEntities) {
            if (mailUnitInfoEntity.getEmails() == null || mailUnitInfoEntity.getEmails().isEmpty()) {
                logger.warn("The `email` is empty in CrawlerManagementMap for the website {}", mailUnitInfoEntity.getSourceWebsite());
                mailUnitInfoEntities.remove(mailUnitInfoEntity);
            }
        }
        //collect by sourceWebsite
        saveToMailPool(mailUnitInfoEntities);
        sendMail();
    }
}
