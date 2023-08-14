package com.crawlerdemo.webmagic.model.zhiliaobiaoxun;

import com.crawlerdemo.webmagic.model.InfoType;
import com.crawlerdemo.webmagic.model.ResultInfoRepo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@TargetUrl("https://\\w+.zhiliaobiaoxun.com/doc/[0-9]+")
@ExtractBy(value = "/html/body/div/div[1]/ul/li", multi = true)
public class ZhiLiaoRepo extends ResultInfoRepo<ZhiLiaoRepo> implements AfterExtractor {

    public static String startUrl = "https://hub.zhiliaobiaoxun.com/doc/1";

    private Integer id;

    private String sourceWebsite = "知了标讯";

    @ExtractBy("h2/a/text()")
    private String title;

    @ExtractBy("h2/a/@href")
    private String url;

    @ExtractByUrl("(.*)")
    private String sourceUrl;

    @ExtractBy(value = "项目地区：([\\u4e00-\\u9fa5]+) [\\u4e00-\\u9fa5]+", type = ExtractBy.Type.Regex)
    private String area;

    @ExtractBy("div/div/text()")
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
    public List<ZhiLiaoRepo> formmatInfo() {
        List<ZhiLiaoRepo> zhiLiaoRepos = new ArrayList<>();
        zhiLiaoRepos.add(this);
        return zhiLiaoRepos;
    }

    @Override
    public void afterProcess(Page page) {
        //format infos
        this.url = "https://hub.zhiliaobiaoxun.com" + this.url;
        this.date = this.date.substring(2, this.date.length() - 1);

        //Add side bar(other provinces)
        page.addTargetRequests(page.getHtml().links().regex("https://\\w+.zhiliaobiaoxun.com/doc/[0-9]+").all());

        //Add next page
        if (!page.getHtml().regex("class=\"selectedPage\">尾页</a>").match()) {
            page.addTargetRequest(this.sourceUrl.substring(0, this.sourceUrl.indexOf("doc/") + 4) + (Integer.parseInt(this.sourceUrl.substring(this.sourceUrl.indexOf("doc/") + 4)) + 1));
        }
    }
}
