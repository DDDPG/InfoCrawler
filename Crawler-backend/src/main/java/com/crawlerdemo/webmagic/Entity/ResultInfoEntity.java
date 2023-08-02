package com.crawlerdemo.webmagic.Entity;

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

    public ResultInfoEntity(ResultInfoRepo resultInfoRepo) {
//        this.id = resultInfoRepo.getId();
        this.sourceWebsite = resultInfoRepo.getSourceWebsite();
        this.title = resultInfoRepo.getTitle();
        this.sourceUrl = resultInfoRepo.getSourceUrl().isBlank() ? "未知" : resultInfoRepo.getSourceUrl();
        this.url = resultInfoRepo.getUrl();
        this.area = resultInfoRepo.getArea().isBlank() ? "未知" : resultInfoRepo.getArea();
        this.date = resultInfoRepo.getDate();
    }
}
