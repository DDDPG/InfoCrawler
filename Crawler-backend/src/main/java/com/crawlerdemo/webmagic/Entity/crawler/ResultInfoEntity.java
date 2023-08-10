package com.crawlerdemo.webmagic.Entity.crawler;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.crawlerdemo.webmagic.model.ResultInfoRepo;
import lombok.Data;

/**
 * Entity class for the result info from table ResultsInfoMap
 */
@Data
@TableName("ResultsInfoMap")
public class ResultInfoEntity {

    @TableId(value = "ID", type = IdType.AUTO)
    private int id;

    @TableField("sourceWebsite")
    private String sourceWebsite;

    @TableField("title")
    private String title;

    @TableField("url")
    private String url;

    @TableField("sourceUrl")
    private String sourceUrl;

    @TableField("area")
    private String area;

    @TableField("date")
    private String date;

    public ResultInfoEntity() {
    }

    public ResultInfoEntity(int id, String sourceWebsite, String title, String url, String sourceUrl, String area, String date) {
        this.id = id;
        this.sourceWebsite = sourceWebsite;
        this.title = title;
        this.url = url;
        this.sourceUrl = sourceUrl;
        this.area = area;
        this.date = date;
    }

    public ResultInfoEntity(ResultInfoRepo resultInfoRepo) {
//        this.id = resultInfoRepo.getId();
        this.sourceWebsite = resultInfoRepo.getSourceWebsite();
        this.title = resultInfoRepo.getTitle();
        this.sourceUrl = resultInfoRepo.getSourceUrl().isBlank() ? "未知" : resultInfoRepo.getSourceUrl();
        this.url = resultInfoRepo.getUrl();
        this.area = resultInfoRepo.getArea().isBlank() ? "未知" : resultInfoRepo.getArea();
        this.date = resultInfoRepo.getDate();
    }

    public ResultInfoEntity(String title, String sourceWebsite, String sourceUrl, String area, String date, String url) {
        this.title = title;
        this.sourceWebsite = sourceWebsite;
        this.sourceUrl = sourceUrl;
        this.area = area;
        this.date = date;
        this.url = url;
    }
}
