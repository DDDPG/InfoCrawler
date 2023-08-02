package com.crawlerdemo.webmagic.model.ccgp_hubei;

import com.crawlerdemo.webmagic.model.InfoType;
import com.crawlerdemo.webmagic.model.ResultInfoRepo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = false)
@Data
@TargetUrl("http://www.ccgp-hubei.gov.cn/notice/cggg/\\w+/index_[0-9]+.html")
@ExtractBy(value = "//*[@id=\"main\"]/div/div/div[2]/div[1]/div/ul/li", multi = true)
public class HuBeiGovRepo extends ResultInfoRepo<HuBeiGovRepo> implements AfterExtractor {

    public static String startUrl = "http://www.ccgp-hubei.gov.cn/notice/cggg/pzbgg/index_1.html";

    private Integer id;

    private String sourceWebsite = "中国政府采购网_湖北省";

    private InfoType type = InfoType.SINGLE;

    @ExtractBy(value = "//a/text()")
    private String title;

    @ExtractBy(value = "//a/@href")
    private String url;

    @ExtractByUrl("(.*)")
    private String sourceUrl;

    //TODO: area is not extracted for the website is specified for Hubei
    private String area = "湖北省";

    @ExtractBy(value = "//span/text()")
    private String date;

    private boolean ifMatching;

    @Override
    public boolean ifMatchingFilter(List<String> filters) {
        for (String filter : filters) {
            if (this.title.contains(filter)) {
                this.ifMatching = true;
                return true;
            }
        }
        this.ifMatching = false;
        return false;
    }

    @Override
    public void afterProcess(Page page) {
        this.title = this.title.substring(2);
        this.url = "http://www.ccgp-hubei.gov.cn" + this.url;

        //add side page for other content blocks
        page.addTargetRequest("http://www.ccgp-hubei.gov.cn/sub/left_memu.jsp?act=01");

        //add next page
        if (this.getTitle() != null) {
            Request request = new Request();
            request.setUrl(this.getSourceUrl().substring(0, this.getSourceUrl().indexOf("_")) + "_" + (Integer.parseInt(page.getUrl().regex("index_(\\d+)").toString()) + 1) + ".html");
            page.addTargetRequest(request);
        }
    }

    @Override
    public List<HuBeiGovRepo> formmatInfo() {
        List<HuBeiGovRepo> list = new ArrayList<>();
        list.add(this);
        return list;
    }

}
