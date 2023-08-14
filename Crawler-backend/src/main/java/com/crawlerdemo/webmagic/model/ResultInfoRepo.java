package com.crawlerdemo.webmagic.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * This class is abstract class for the result info from each website.
 */
@Data
public abstract class ResultInfoRepo<T> implements Serializable {

    transient Integer id;

    //The sourceWebsite in graph CrawlerManagement
    transient String sourceWebsite;

    transient String title;

    transient String url;

    transient String sourceUrl;

    transient String area;

    transient String date;

    public abstract boolean ifMatchingFilter(List<String> filter);

    /**
     * This method is used to format the info from the website to the ResultInfoRepo.
     */
    public abstract List<T> formmatInfo();

}
