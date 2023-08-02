package com.crawlerdemo.webmagic.model.bibenet;

import com.crawlerdemo.webmagic.model.InfoType;
import com.crawlerdemo.webmagic.model.ResultInfoRepo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@TargetUrl("https://www.bibenet.com/zbggu[0-9]+.html")
@ExtractBy(value = "//table[@class='secondary_detail']/tbody/tr", multi = true)
public class BiBinetRepo extends ResultInfoRepo<BiBinetRepo> implements AfterExtractor {

    public static String startUrl = "https://www.bibenet.com/zbggu1.html";

    private Integer id;

    private InfoType type = InfoType.SINGLE;

    private String  sourceWebsite = "比比网";

    @ExtractBy(value = "//div[@class='fl']/a/text()", notNull = true)
    private String title;

    @ExtractBy("//div[@class='fl']/a/@href")
    private String url;

    @ExtractByUrl("(.*)")
    private String sourceUrl;

    @ExtractBy(value = "td[3]")
    private String area;

    @ExtractBy("td[@class='last']/text()")
    private String date;

    private boolean ifMatching;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(BiBinetRepo.class);

    @Override
    public void afterProcess(Page page) {

        //For the area, we need to remove the html tags.
        //Because the framework didn't support index in xpath
        this.area = this.area.substring(4, this.area.length() - 5);

        page.addTargetRequests(page.getHtml().links().regex("https://www.bibenet.com/zbggu[0-9]+.html").all());
    }


    @Override
    public boolean ifMatchingFilter(List<String> filters) {
        for (String filter : filters) {
            if (this.title.contains(filter)) {
                this.ifMatching = true;
//                logger.info("Matched! The title {} contains the filter {}", this.title, filter);
                return true;
            }
        }
        this.ifMatching = false;
//        logger.info("Not matched! The title {} doesn't contain any filter", this.title);
        return false;
    }

    @Override
    public List<BiBinetRepo> formmatInfo() {
        List<BiBinetRepo> biBinetRepos = new ArrayList<>();
        biBinetRepos.add(this);
        return biBinetRepos;
    }

}