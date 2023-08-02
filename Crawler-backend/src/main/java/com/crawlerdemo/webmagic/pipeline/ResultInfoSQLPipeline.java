package com.crawlerdemo.webmagic.pipeline;

import com.crawlerdemo.webmagic.Entity.CrawlerManagementEntity;
import com.crawlerdemo.webmagic.Entity.ResultInfoEntity;
import com.crawlerdemo.webmagic.config.CrawlerManagementConfig;
import com.crawlerdemo.webmagic.model.ResultInfoPool;
import com.crawlerdemo.webmagic.model.ResultInfoRepo;
import com.crawlerdemo.webmagic.service.ResultSQLService;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.util.ArrayList;
import java.util.List;

@Component("ResultInfoSQLPipeline")
public class ResultInfoSQLPipeline implements PageModelPipeline<ResultInfoRepo> {

    private final CrawlerManagementConfig companyInfoFilterConfiguration;
    private final ResultSQLService resultSqlService;
    private final ResultInfoPool companyInfoPool;

    public ResultInfoSQLPipeline(CrawlerManagementConfig companyInfoFilterConfiguration, ResultSQLService resultSqlService, ResultInfoPool companyInfoPool) {
        this.companyInfoFilterConfiguration = companyInfoFilterConfiguration;
        this.resultSqlService = resultSqlService;
        this.companyInfoPool = companyInfoPool;
    }

    @Override
    public void process(ResultInfoRepo resultInfoRepo, Task task) {
        // 获取管理实体
        CrawlerManagementEntity crawlerManagementEntity = companyInfoFilterConfiguration.getManagementEntity(resultInfoRepo);

        // 获取过滤器列表
        List<String> filterList = getFilters(resultInfoRepo, crawlerManagementEntity);

        // 添加信息到信息池并进行预检查
        formatAndPreCheck(resultInfoRepo);

        // 处理信息
        processResultInfoRepoList(filterList);

        // 清空信息池并进行后检查
        cleanAndAfterCheck();
    }

    private void processResultInfoRepoList(List<String> filterList) {
        List<ResultInfoRepo> infoRepoList = new ArrayList<>(companyInfoPool.getResultInfoRepoPool());
        for (ResultInfoRepo infoRepo : infoRepoList) {
            // 匹配过滤器，如果不匹配则跳过
            if (infoRepo.ifMatchingFilter(filterList)) {
                ResultInfoEntity resultInfoEntity = new ResultInfoEntity(infoRepo);

                // 如果匹配，将其保存到 SQL 数据库
                int returnValue = resultSqlService.saveToDatabase(resultInfoEntity);

                // 如果 SQL 插入失败，抛出异常
                if (returnValue == 0) {
                    throw new RuntimeException("SQL insert failed: " + resultInfoEntity);
                }
            }
        }
    }

    private void cleanAndAfterCheck() {
        companyInfoPool.clearCompanyRepo();
        companyInfoPool.afterCheck();
    }

    private void formatAndPreCheck(ResultInfoRepo resultInfoRepo) {
        companyInfoPool.addCompanyRepos(resultInfoRepo.formmatInfo());
        companyInfoPool.preCheck();
    }

    private List<String> getFilters(ResultInfoRepo resultInfoRepo, CrawlerManagementEntity crawlerManagementEntity) {
        String filters = crawlerManagementEntity.getCrawlerKeywords();
        if (filters.isBlank()) {
            throw new RuntimeException("No filters for websiteName: " + resultInfoRepo.getSourceWebsite());
        }
        return List.of(filters.split(","));
    }
}
