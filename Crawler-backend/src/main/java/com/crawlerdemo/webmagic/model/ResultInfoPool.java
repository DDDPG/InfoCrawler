package com.crawlerdemo.webmagic.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class ResultInfoPool {

    private final List<ResultInfoRepo> resultInfoRepoPool = new ArrayList<>();

    public void addCompanyRepo(ResultInfoRepo resultInfoRepo) {
        this.resultInfoRepoPool.add(resultInfoRepo);
    }

    public void addCompanyRepos(List<ResultInfoRepo> resultInfoRepoList) {
        this.resultInfoRepoPool.addAll(resultInfoRepoList);
    }

    public void removeCompanyRepo(ResultInfoRepo resultInfoRepo) {
        this.resultInfoRepoPool.remove(resultInfoRepo);
    }

    public void removeCompanyRepos(List<ResultInfoRepo> resultInfoRepoList) {
        this.resultInfoRepoPool.removeAll(resultInfoRepoList);
    }

    public void clearCompanyRepo() {
        this.resultInfoRepoPool.clear();
    }


    /**
     * This method is used to check the data before saving to the database.
     */
    public void preCheck() {
    }

    public void afterCheck() {
    }
}
