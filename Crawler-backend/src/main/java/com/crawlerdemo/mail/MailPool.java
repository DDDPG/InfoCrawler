package com.crawlerdemo.mail;

import com.crawlerdemo.webmagic.Entity.crawler.MailUnitInfoEntity;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Component
public class MailPool {
    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(MailPool.class);

    private List<MailUnitInfoEntity> mailUnitInfoEntities = new ArrayList<>();

    @Autowired
    private JavaMailSender mailSender;

    private List<MimeMessage> mimeMessages = new ArrayList<>();

    @Value("${spring.mail.username}")
    private String from;

    public void sendMails() {
        for (MimeMessage mimeMessage : mimeMessages) {
            if (ifCompleteInfo(mimeMessage)) {
                mailSender.send(mimeMessage);
                logger.info("Send mails successfully");
            }
        }
    }

    private boolean ifCompleteInfo(MimeMessage mimeMessage) {
        //Check the info for each mail, if missed, return false and log it
        try {
            if (mimeMessage.getSubject() == null || mimeMessage.getSubject().isEmpty()) {
                logger.error("The subject is empty for {}", mimeMessage);
                return false;
            }
            if (mimeMessage.getRecipients(MimeMessage.RecipientType.TO) == null || mimeMessage.getRecipients(MimeMessage.RecipientType.TO).length == 0) {
                logger.error("The recipient is empty for {}", mimeMessage);
                return false;
            }
            if (mimeMessage.getContent() == null || mimeMessage.getContent().toString().isEmpty()) {
                logger.error("The content is empty for {}", mimeMessage);
                return false;
            }
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void clear() {
        this.mailUnitInfoEntities.clear();
        this.mimeMessages.clear();
        logger.info("Mail pool cleared");
    }

    public void collectMails() {
        //Grouped mails by its sourceWebsite in Map format, key: sourceWebsite, value: List<MailUnitInfoEntity>
        Map<String, List<MailUnitInfoEntity>> groupedMails = mailUnitInfoEntities.stream().collect(Collectors.groupingBy(MailUnitInfoEntity::getSourceWebsite));

        for (Map.Entry<String, List<MailUnitInfoEntity>> entry : groupedMails.entrySet()) {
            generateMail(entry.getKey(), entry.getValue());
            logger.info("The mail is: " + entry.getKey() + " " + entry.getValue() + "\n");
        }

    }

    private void generateMail(String key, List<MailUnitInfoEntity> mailUnitInfoList) {
        try {
            if (mailUnitInfoList.size() == 0) {
                logger.info("No mail for the website: " + key);
                return;
            }

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            //generate head info
            helper.setSubject("筛选信息来自: " + key);
            helper.setFrom(from);

            //one website for one group receiver
            if (Optional.ofNullable(mailUnitInfoList.get(0).getEmails()).orElse("").equals("")) {
                logger.info("No receiver for the website: " + key);
                return;
            }
            helper.setTo(mailUnitInfoList.get(0).getEmails().split(","));
            helper.setText("来自" + key + "的信息, 请查收, 爬虫执行时间: " + LocalDateTime.now().toLocalDate());
            //generate excel file
            byte[] excelData = createExcelFile(mailUnitInfoList);
            DataSource dataSource = new ByteArrayDataSource(excelData, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            helper.addAttachment("Infos from the website: " + key + ".xlsx", dataSource);

            //Add to ready-to-send list
            this.mimeMessages.add(mimeMessage);

            logger.info("Generate mail for the website: " + key);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] createExcelFile(List<MailUnitInfoEntity> mailList) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Mail Unit Info");
            int rowIndex = 0;

            // Create header row
            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.createCell(0).setCellValue("文章链接");
            headerRow.createCell(1).setCellValue("发布日期");
            headerRow.createCell(2).setCellValue("地区");
            headerRow.createCell(3).setCellValue("信息来源");
            headerRow.createCell(4).setCellValue("文章标题");
            headerRow.createCell(5).setCellValue("关键词");

            // Fill data rows
            for (MailUnitInfoEntity mail : mailList) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(mail.getUrl());
                dataRow.createCell(1).setCellValue(mail.getDate());
                dataRow.createCell(2).setCellValue(mail.getArea());
                dataRow.createCell(3).setCellValue(mail.getSourceWebsite());
                dataRow.createCell(4).setCellValue(mail.getTitle());
                dataRow.createCell(5).setCellValue(mail.getCrawlerKeywords());
            }

            // Write to ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
