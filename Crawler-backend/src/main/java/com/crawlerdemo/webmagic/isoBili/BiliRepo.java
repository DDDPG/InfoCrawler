package com.crawlerdemo.webmagic.isoBili;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

@Data
public class BiliRepo implements PageProcessor, AfterExtractor {

    private Site site = Site.me().setRetryTimes(5).setSleepTime(500);

    @ExtractBy("//body/text()")
    private String textRaw;

    private JSONArray jsonArray;

    private List<BiliUnitRepo> biliUnitRepoList = new ArrayList<>();

    @Override
    public void process(Page page) {
        page.putField("textRaw", page.getHtml().xpath("//body/text()").toString());
        textRaw = page.getHtml().xpath("//body/text()").toString();
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    @Override
    public void afterProcess(Page page) {
//        System.out.println(this.textRaw);
        this.setJsonArray(JSONObject.parseObject(this.getTextRaw()).getJSONObject("data").getJSONArray("archives"));

        //Add unit
        for (int i = 0; i < this.getJsonArray().size(); i++) {
            JSONObject jsonObject = this.getJsonArray().getJSONObject(i);
            BiliUnitRepo biliUnitRepo = new BiliUnitRepo();
            biliUnitRepo.setTitle(jsonObject.getString("title"));
            biliUnitRepo.setUrl("https://www.bilibili.com/" + jsonObject.getString("bvid"));
            this.getBiliUnitRepoList().add(biliUnitRepo);
        }

        //Add next page
        int currentPage = JSONObject.parseObject(this.getTextRaw()).getJSONObject("data").getJSONObject("page").getInteger("num");
        int totalPage = JSONObject.parseObject(this.getTextRaw()).getJSONObject("data").getJSONObject("page").getInteger("size");
        for (int i = currentPage + 1; i <= totalPage; i++) {
            Request request = new Request();
            request.setUrl("https://api.bilibili.com/x/series/archives?mid=3461563583302074&series_id=2704247&only_normal=true&sort=desc&pn=" + i + "&ps=" + totalPage);
            page.addTargetRequest(request);
        }
    }

    public static void main(String[] args) {
        OOSpider.create(Site.me().setRetryTimes(5).setSleepTime(500), new ConsolePageModelPipeline(), BiliRepo.class)
                .addUrl("https://api.bilibili.com/x/series/archives?mid=3461563583302074&series_id=2704247&only_normal=true&sort=desc&pn=1&ps=30")
                .thread(5)
                .run();
    }
}
