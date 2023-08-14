package com.crawlerdemo.webmagic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crawlerdemo.webmagic.Entity.crawler.ResultInfoEntity;
import com.crawlerdemo.webmagic.service.ResultSQLService;
import lombok.Data;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
/**
 *  This class is used to operate the result information table from `infotable` in vue view and `ResultsInfoMap` in database.
 */
@RestController
@RequestMapping("/api/v1/resultInfoTable")
public class InfoTableOperationController {

    @Autowired
    private ResultSQLService resultSQLService;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ResultSQLService.class);

    /**
     * 插入结果信息表
     * @param title: 标题
     * @param sourceWebsite: 来源网站
     * @param sourceUrl: 来源网址
     * @param area: 地区
     * @param date: 日期
     * @param url: 网址
     * @return NormalSQLResponse: 插入结果信息表的响应体
     */
    @PostMapping
    public NormalSQLResponse insertResultInfoTable(String title,
                                                   String sourceWebsite,
                                                   String sourceUrl,
                                                   String area,
                                                   String date,
                                                   String url) {
        ResultInfoEntity resultInfoEntity = new ResultInfoEntity(title, sourceWebsite, sourceUrl, area, date, url);

        int result = resultSQLService.insertResultInfoTable(resultInfoEntity);

        NormalSQLResponse normalSQLResponse = new NormalSQLResponse();
        if (result == 1) {
            normalSQLResponse.setCode(0);
            normalSQLResponse.setMessage("插入成功");
        } else {
            normalSQLResponse.setCode(1);
            normalSQLResponse.setMessage("插入失败");
        }
        logger.warn("插入结果：{}", normalSQLResponse);
        return normalSQLResponse;
    }

    /**
     * 删除结果信息表
     * @param id: 要删除的结果信息表的id
     * @return NormalSQLResponse: 删除结果信息表的响应体
     */
    @DeleteMapping("/{id}")
    public NormalSQLResponse deleteResultInfoTable(@PathVariable Integer id) {
        int result = resultSQLService.deleteResultInfoTable(id);
        NormalSQLResponse normalSQLResponse = new NormalSQLResponse();
        if (result == 1) {
            normalSQLResponse.setCode(0);
            normalSQLResponse.setMessage("删除成功");
        } else {
            normalSQLResponse.setCode(1);
            normalSQLResponse.setMessage("删除失败");
        }
        logger.warn("删除结果：{}", normalSQLResponse);
        return normalSQLResponse;
    }

    /**
     * 更新结果信息表
     * @param id: 要更新的结果信息表的id
     * @param title: 标题
     * @param sourceWebsite: 来源网站
     * @param sourceUrl: 来源网址
     * @param area: 地区
     * @param date: 日期
     * @param url: 网址
     * @return NormalSQLResponse: 更新结果信息表的响应体
     */
    @PutMapping
    public NormalSQLResponse updateResultInfoTable(Integer id,
                                                   String title,
                                                   String sourceWebsite,
                                                   String sourceUrl,
                                                   String area,
                                                   String date,
                                                   String url) {
        ResultInfoEntity resultInfoEntity = new ResultInfoEntity(id, title, sourceWebsite, sourceUrl, area, date, url);

        int result = resultSQLService.updateResultInfoTable(resultInfoEntity);

        NormalSQLResponse normalSQLResponse = new NormalSQLResponse();
        if (result == 1) {
            normalSQLResponse.setCode(0);
            normalSQLResponse.setMessage("更新成功");
        } else {
            normalSQLResponse.setCode(1);
            normalSQLResponse.setMessage("更新失败");
        }
        logger.warn("更新结果：{}", normalSQLResponse);
        return normalSQLResponse;
    }

    /**
     * 查询结果信息表
     * @param currentPage: 当前页码
     * @param size: 每页显示的条数
     * @param title: 标题
     * @param sourceWebsite: 来源网站
     * @param area: 地区
     * @param startDate: 筛选起始日期
     * @param endDate: 筛选截止日期
     * @return SelectSQLResponse: 查询结果信息表的响应体
     */
    @GetMapping
    public SelectSQLResponse getResultInfoTable(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                @RequestParam(value = "title", defaultValue = "") String title,
                                                @RequestParam(value = "sourceWebsite", defaultValue = "") String sourceWebsite,
                                                @RequestParam(value = "area", defaultValue = "") String area,
                                                @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                                @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        Page<ResultInfoEntity> page = resultSQLService.selectResultInfoTable(currentPage, size, title, sourceWebsite, area, startDate, endDate);
        List<ResultInfoEntity> resultInfoEntityList = page.getRecords();
        long total = page.getTotal();

        SelectSQLResponse selectSQLResponse = new SelectSQLResponse();
        selectSQLResponse.setCode(0);
        selectSQLResponse.setMessage("success");
        selectSQLResponse.setData(new SelectSQLResponse.Data(resultInfoEntityList, total));

//        logger.warn("查询结果：{}", selectSQLResponse);
        return selectSQLResponse;
    }

    @Data
    static class NormalSQLResponse implements Serializable {
        private int code;
        private String message;
        private Data data;

        public NormalSQLResponse() {
        }

        public NormalSQLResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }
        static class Data implements Serializable {
            public Data() {
            }
        }
    }

    @Data
    static class SelectSQLResponse implements Serializable{
        private int code;
        private String message;
        private Data data;

        public SelectSQLResponse() {
        }

        public SelectSQLResponse(int code, String message, Data data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        @lombok.Data
        static
        class Data implements Serializable {
            private List<ResultInfoEntity> list;
            private long total;

            public Data() {
            }

            public Data(List<ResultInfoEntity> resultInfoEntityList, long total) {
                this.list = resultInfoEntityList;
                this.total = total;
            }
        }
    }
}
