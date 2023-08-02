package com.crawlerdemo.webmagic.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("crawlersetting")
public class CrawlerSettingEntity {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer ID;

    @TableField("settingName")
    private String settingName;

    @TableField("maxRetryTimes")
    private Integer maxRetryTimes;

    @TableField("threadNum")
    private Integer threadNum;

    @TableField("timeout")
    private Integer timeout;

    @TableField("sleepTime")
    private Integer sleepTime;

    @TableField("status")
    private Integer status;
}