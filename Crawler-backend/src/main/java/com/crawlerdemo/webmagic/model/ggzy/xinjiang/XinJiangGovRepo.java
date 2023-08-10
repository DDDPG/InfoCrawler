package com.crawlerdemo.webmagic.model.ggzy.xinjiang;

import com.crawlerdemo.webmagic.model.InfoType;
import com.crawlerdemo.webmagic.model.ResultInfoRepo;
import com.crawlerdemo.webmagic.model.zhiliaobiaoxun.ZhiLiaoRepo;
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
@ExtractBy(value = "//*[@id=\"main\"]/tr", multi = true)
public class XinJiangGovRepo extends ResultInfoRepo<XinJiangGovRepo> implements AfterExtractor {
    public static String startUrl = "https://ggzy.xinjiang.gov.cn/xinjiangggzy/xxzx/011001/information.html";

    private Integer id;

    private InfoType type = InfoType.SINGLE;

    private String sourceWebsite = "新疆公共资源交易网";

    @ExtractBy("td/a/text()")
    private String title;

    @ExtractBy("td/a/@href")
    private String url;

    @ExtractByUrl("(.*)")
    private String sourceUrl;

    private String area="新疆维吾尔自治区";

    @ExtractBy(value = "td[2]")
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
    public List<XinJiangGovRepo> formmatInfo() {
        List<XinJiangGovRepo> list = new ArrayList<>();
        list.add(this);
        return list;
    }

    @Override
    public void afterProcess(Page page) {
        //format infos
        this.url = "https://ggzy.xinjiang.gov.cn" + this.url;
        this.date = this.date.substring(4, this.date.length() - 5);

        //add next page
        page.addTargetRequests(page.getHtml().links().regex("https://ggzy.xinjiang.gov.cn/xinjiangggzy/\\w+/[0-9]+/[0-9]+.html").all());

    }

    public static void main(String[] args) {
        OOSpider.create(Site.me().setRetryTimes(5), new ConsolePageModelPipeline(), XinJiangGovRepo.class)
                .addUrl(XinJiangGovRepo.startUrl)
                .thread(5)
                .run();
    }
}
