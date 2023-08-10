package com.crawlerdemo.webmagic.model.ggzy.nanning;

import com.crawlerdemo.webmagic.model.InfoType;
import com.crawlerdemo.webmagic.model.ResultInfoRepo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@TargetUrl("http://ggzy.nanning.gov.cn/zcfg/zfcg/index_\\d+.html")
@ExtractBy(value = "/html/body/div[4]/div/div[3]/div[1]/div[2]/div[2]/ul/li", multi = true)
public class NanNingGovRepo extends ResultInfoRepo<NanNingGovRepo> implements AfterExtractor {
    public static String startUrl = "http://ggzy.nanning.gov.cn/zcfg/zfcg/index_1.html";

    private Integer id;

    private InfoType type = InfoType.SINGLE;

    private String sourceWebsite = "南宁市公共资源交易网";

    @ExtractBy("li/a/text()")
    private String title;

    @ExtractBy("li/a/@href")
    private String url;

    @ExtractByUrl("(.*)")
    private String sourceUrl;

    private String area = "广西省";

    @ExtractBy(value = "\\d{4}-\\d{1,2}-\\d{1,2}", type = ExtractBy.Type.Regex)
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
    public List<NanNingGovRepo> formmatInfo() {
        List<NanNingGovRepo> list = new ArrayList<>();
        list.add(this);
        return list;
    }

    @Override
    public void afterProcess(Page page) {
        //format info
        this.url = "http://ggzy.nanning.gov.cn/zcfg/zfcg" + this.url.substring(2);
        //add next page
        int pageNum = Integer.parseInt(page.getUrl().regex("index_(\\d+).html").toString());
        page.addTargetRequest("http://ggzy.nanning.gov.cn/zcfg/zfcg/index_" + (pageNum + 1) + ".html");
    }

    public static void main(String[] args) {
        OOSpider.create(Site.me().setRetryTimes(3).setSleepTime(500),
                        new ConsolePageModelPipeline(),
                        NanNingGovRepo.class)
                .addUrl(startUrl)
                .thread(5)
                .run();
    }
}
