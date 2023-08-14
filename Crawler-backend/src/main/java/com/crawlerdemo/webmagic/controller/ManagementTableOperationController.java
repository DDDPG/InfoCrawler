package com.crawlerdemo.webmagic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crawlerdemo.webmagic.Entity.crawler.CrawlerManagementEntity;
import com.crawlerdemo.webmagic.service.ManagementSQLService;
import lombok.Data;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * This class is used to operate the management information table from `managementtable` in vue view and `CrawlerManagement` in database.
 */
@RestController
@RequestMapping("/api/v1/crawlerManagementTable")
public class ManagementTableOperationController {
    @Autowired
    private ManagementSQLService managementSQLService;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ManagementSQLService.class);

    /**
     * 添加管理信息表
     * @param ifCrawl: 是否爬取
     * @param websiteName: 网站名称
     * @param homePage: 网站首页
     * @param contact: 联系人
     * @param emails: 邮箱
     * @param crawlerKeywords: 爬取关键词
     */
    @PostMapping
    public NormalSQLResponse addManagementTable(@RequestParam(value = "ifCrawl", defaultValue = "false") Boolean ifCrawl,
                                                @RequestParam(value = "websiteName", defaultValue = "") String websiteName,
                                                @RequestParam(value = "homePage", defaultValue = "") String homePage,
                                                @RequestParam(value = "contact", defaultValue = "") String contact,
                                                @RequestParam(value = "emails", defaultValue = "") String emails,
                                                @RequestParam(value = "crawlerKeywords", defaultValue = "") String crawlerKeywords) {
        CrawlerManagementEntity crawlerManagementEntity = new CrawlerManagementEntity(ifCrawl?1:0, websiteName, homePage, contact, emails, crawlerKeywords);
        int result = managementSQLService.addManagementTable(crawlerManagementEntity);
        NormalSQLResponse normalSQLResponse = new NormalSQLResponse();
        if (result == 1) {
            normalSQLResponse.setCode(0);
            normalSQLResponse.setMessage("添加成功");
        } else {
            normalSQLResponse.setCode(1);
            normalSQLResponse.setMessage("添加失败");
        }
        logger.warn("添加结果：{}", normalSQLResponse);
        return normalSQLResponse;
    }

    /**
     * 删除管理信息表
     * @param id: 要删除的管理信息表的id
     */
    @DeleteMapping("/{id}")
    public NormalSQLResponse deleteManagementTable(@PathVariable Integer id) {
        int result = managementSQLService.deleteManagementTable(id);
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
     * 修改管理信息表
     * @param id: 要修改的管理信息表的id
     * @param ifCrawl: 是否爬取
     * @param websiteName: 网站名称
     * @param homePage: 网站首页
     * @param contact: 联系人
     * @param emails: 邮箱
     * @param crawlerKeywords: 爬取关键词
     */
    @PutMapping
    public NormalSQLResponse updateManagementTable(Integer id,
                                                   Boolean ifCrawl,
                                                   String websiteName,
                                                   String homePage,
                                                   String contact,
                                                   String emails,
                                                   String crawlerKeywords) {
        CrawlerManagementEntity crawlerManagementEntity = new CrawlerManagementEntity(id, ifCrawl?1:0, websiteName, homePage, contact, emails, crawlerKeywords);
        int result = managementSQLService.updateManagementTable(crawlerManagementEntity);
        NormalSQLResponse normalSQLResponse = new NormalSQLResponse();
        if (result == 1) {
            normalSQLResponse.setCode(0);
            normalSQLResponse.setMessage("修改成功");
        } else {
            normalSQLResponse.setCode(1);
            normalSQLResponse.setMessage("修改失败");
        }
        logger.warn("修改结果：{}", normalSQLResponse);
        return normalSQLResponse;
    }

    /**
     * 查询管理信息表
     * @param currentPage: 当前页码
     * @param size: 每页显示的条数
     * @param ifCrawl: 是否爬取
     * @param websiteName: 网站名称
     * @param homePage: 网站首页
     * @param contact: 联系人
     * @param emails: 邮箱
     * @param crawlerKeywords: 爬取关键词
     */
    @GetMapping
    public SelectSQLResponse getManagementTable(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                @RequestParam(value = "ifCrawl", required = false) Boolean ifCrawl,
                                                @RequestParam(value = "websiteName", defaultValue = "") String websiteName,
                                                @RequestParam(value = "homePage", defaultValue = "") String homePage,
                                                @RequestParam(value = "contact", defaultValue = "") String contact,
                                                @RequestParam(value = "emails", defaultValue = "") String emails,
                                                @RequestParam(value = "crawlerKeywords", defaultValue = "") String crawlerKeywords) {

        Page<CrawlerManagementEntity> page = managementSQLService.selectManagementTable(currentPage, size, ifCrawl, websiteName, homePage, contact, emails, crawlerKeywords);
        List<CrawlerManagementEntity> managementInfoEntityList = page.getRecords();
        long total = page.getTotal();

        SelectSQLResponse selectSQLResponse = new SelectSQLResponse();
        selectSQLResponse.setCode(0);
        selectSQLResponse.setMessage("success");
        selectSQLResponse.setData(new SelectSQLResponse.Data(managementInfoEntityList, total));

        logger.warn("查询结果：{}", selectSQLResponse);
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
            private List<CrawlerManagementEntity> list;
            private long total;

            public Data() {
            }

            public Data(List<CrawlerManagementEntity> resultInfoEntityList, long total) {
                this.list = resultInfoEntityList;
                this.total = total;
            }
        }
    }
}
