package com.crawlerdemo.webmagic.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crawlerdemo.utils.DateUtils;
import com.crawlerdemo.utils.StringUtils;
import com.crawlerdemo.webmagic.Entity.crawler.ResultInfoEntity;
import com.crawlerdemo.webmagic.mapper.crawlermapper.ResultInfoMapper;
import jakarta.annotation.Resource;
import lombok.Data;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
@EnableCaching
public class ResultSQLService {

    @Resource
    private ResultInfoMapper resultInfoMapper;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ResultSQLService.class);

    @Transactional
    @Cacheable(cacheNames = "CrawlerResultInfo", key = "#resultInfoEntity != null ? #resultInfoEntity : 'defaultKey'", unless = "#result == null")
    public int saveToDatabase(ResultInfoEntity resultInfoEntity) {
        return resultInfoMapper.filterAndInsert(resultInfoEntity);
    }

    @Transactional
    public Page<ResultInfoEntity> selectResultInfoTable(int currentPage, int size, String title, String sourceWebsite, String area, String startDate, String endDate) {
        // 创建分页对象
        Page<ResultInfoEntity> page = new Page<>(currentPage, size);

        // 创建查询条件构造器
        QueryWrapper<ResultInfoEntity> queryWrapper = new QueryWrapper<>();

        // 设置标题模糊查询条件（如果有提供标题参数）
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }

        // 设置来源网站精确匹配条件（如果有提供来源网站参数）
        if (StringUtils.isNotBlank(sourceWebsite)) {
            queryWrapper.eq("sourceWebsite", sourceWebsite);
        }

        // 设置地区精确匹配条件（如果有提供地区参数）
        if (StringUtils.isNotBlank(area)) {
            queryWrapper.eq("area", area);
        }

        // 设置日期范围查询条件（如果有提供起始日期和结束日期参数）
        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            queryWrapper.between("date",
                    DateUtils.parseDate(startDate),
                    DateUtils.parseDate(endDate));
        }
        queryWrapper.select("ID", "title", "url",  "area", "sourceWebsite", "sourceUrl", "date");

        return resultInfoMapper.selectPage(page, queryWrapper);
    }

    @Transactional
    public int deleteResultInfoTable(int id) {
        return resultInfoMapper.deleteById(id);
    }

    @Transactional
    public int updateResultInfoTable(ResultInfoEntity resultInfoEntity) {
        return resultInfoMapper.updateById(resultInfoEntity);
    }

    @Transactional
    public int insertResultInfoTable(ResultInfoEntity resultInfoEntity) {
        return resultInfoMapper.insert(resultInfoEntity);
    }
}
